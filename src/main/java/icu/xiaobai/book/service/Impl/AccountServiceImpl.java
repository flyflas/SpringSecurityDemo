package icu.xiaobai.book.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import icu.xiaobai.book.entity.UserInfo;
import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.entity.User;
import icu.xiaobai.book.mapper.UserMapper;
import icu.xiaobai.book.service.SendEmailService;
import icu.xiaobai.book.service.AccountService;
import icu.xiaobai.book.util.RedisUtil;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final SendEmailService sendEmailService;
    private final UserDetailsService userDetailsService;

    public AccountServiceImpl(RedisUtil redisUtil, PasswordEncoder passwordEncoder, UserMapper userMapper, SendEmailService sendEmailService, UserDetailsService userDetailsService) {
        this.redisUtil = redisUtil;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.sendEmailService = sendEmailService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 用户密码修改函数
     * 步骤：
     * 1. 通过 key，从 Redis 中获取email
     *
     * @param key 唯一标识
     * @return 200 链接有效，允许修改密码。
     * 401 链接过期或者无效。
     */
    @Override
    public CustomResponse<String> resetPassword(String key) {
        String email = redisUtil.getResetPasswordInfo(key);

        /* 链接过期或者无效 */
        if (Objects.isNull(email)) {
            return new CustomResponse<>(HttpStatus.UNAUTHORIZED);
        }

        return new CustomResponse<>(HttpStatus.OK);
    }

    /**
     * 用户注册函数
     * 1. 将用户密码加密
     * 2. 发送激活邮件
     *
     * @param user 待注册的用户信息
     * @return 200 注册成功
     */
    @Override
    public CustomResponse<String> addUser(User user) throws MessagingException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);

        sendEmailService.sendUserConfirmEmail(user.getEmail());

        return new CustomResponse<>(HttpStatus.OK);
    }

    /**
     * 邮箱验证函数
     * 用于验证新注册用户的邮箱，是否真实有效
     * 步骤：
     * 1. 从 Redis 中根据 key，获得Email。如果 email不存在，返回401
     * 2. 更改用户账户状态 2 -> 1
     *
     * @param key 唯一标识
     * @return 200 验证成功， 401 链接失效或者URL非法
     */
    @Override
    public CustomResponse<String> checkSignUpEmail(String key) {
        String email = redisUtil.getAccountActivationInfo(key);

        if (Objects.isNull(email)) {
            return new CustomResponse<>(HttpStatus.UNAUTHORIZED);
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getStatus, 1).eq(User::getEmail, email);
        userMapper.update(null, wrapper);

        return new CustomResponse<>(HttpStatus.OK);
    }

    /**
     * 用户账号状态函数
     */
    @Override
    public CustomResponse<Map<String, String>> getAccountStatus() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer status = userInfo.getUser().getStatus();
        String msg = switch (status) {
            case 101 -> "正常";
            case 201 -> "账户未经验证";
            case 202 -> "因为安全原因暂停账户";
            case 203 -> "其他原因暂停账户";
            case 301 -> "账户封禁";
            default -> null;
        };
        Map<String, String> map = new HashMap<>();
        map.put("code", Integer.toString(status));
        map.put("msg", msg);
        return new CustomResponse<Map<String, String>>(HttpStatus.OK, map);
    }

    /**
     * 用户账户激活函数
     * 用于注册之后，账户未激活。之后请求激活的情景
     *
     * @return 200 成功发送激活你有劲啊。400 非法请求
     */
    @Override
    public CustomResponse<String> activateAccount() throws MessagingException {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, userInfo.getUser().getEmail());
        User user = userMapper.selectOne(wrapper);

        if (!Objects.isNull(user) && user.getStatus() == 201) {
            // send email
            sendEmailService.sendUserConfirmEmail(userInfo.getUser().getEmail());
            return new CustomResponse<>(HttpStatus.OK);
        }

        return new CustomResponse<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 邮件重复检查函数
     * 主要用于注册时，检查邮箱是否已经被注册
     *
     * @param email 需要检查的邮箱
     * @return true 该邮箱已经被注册。false 该邮箱未被注册
     */
    @Override
    public boolean checkEmailUsed(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email).select(User::getEmail);
        User user = userMapper.selectOne(wrapper);
        return !Objects.isNull(user);
    }

    @Override
    public CustomResponse<String> getLimitedToken(String email, String password) throws JsonProcessingException {
        CustomResponse<String> response = null;
        try {
            UserInfo userInfo = (UserInfo) userDetailsService.loadUserByUsername(email);
            if (passwordEncoder.matches(password, userInfo.getPassword())) {
                // 用户名密码正确
                String token = redisUtil.saveUserInfo(userInfo);
                response = new CustomResponse<String>(HttpStatus.OK, token);
            } else {
                response = new CustomResponse<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (UsernameNotFoundException e) {
            response = new CustomResponse<>(HttpStatus.UNAUTHORIZED);
        }

        return response;
    }
}
