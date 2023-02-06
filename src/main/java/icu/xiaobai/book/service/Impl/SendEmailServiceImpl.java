package icu.xiaobai.book.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.entity.User;
import icu.xiaobai.book.mapper.UserMapper;
import icu.xiaobai.book.service.SendEmailService;
import icu.xiaobai.book.util.EmailUtil;
import icu.xiaobai.book.util.RedisUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SendEmailServiceImpl implements SendEmailService {
    private final RedisUtil redisUtil;
    private final EmailUtil emailUtil;
    //    private final EmailService emailService;
    private final UserMapper userMapper;
    @Value("${spring.mail.reset-password-url}")
    private String resetPasswordUrl;
    @Value("${spring.mail.account-activation-url}")
    private String accountActivationUrl;

    public SendEmailServiceImpl(RedisUtil redisUtil, EmailUtil emailService, UserMapper userMapper) {
        this.redisUtil = redisUtil;
        this.emailUtil = emailService;
        this.userMapper = userMapper;
    }

    /**
     * 密码重置邮件发送函数。
     * 步骤：
     * 1. 检查用户是否存在，不存在返回401.
     * 2. 生成UUID，并将其保存在Redis中
     * 3. 发送重置邮件
     *
     * @param email 需要重置用户的邮箱
     * @param ip    用户请求重置密码时的ip
     * @return 200 密码重置邮件发送成功
     * 401 该用户不存在
     * @throws MessagingException 密码重置邮件模板异常
     */
    @Override
    public CustomResponse<String> sendResetPasswordEmail(String email, String ip) throws MessagingException {
        // 验证用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(wrapper);
        if (Objects.isNull(user)) {
            return new CustomResponse<>(HttpStatus.UNAUTHORIZED);
        }

        // 生成邮件
        String link = redisUtil.saveResetPasswordInfo(email);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Map<String, Object> emailInfo = new HashMap<>();
        emailInfo.put("resetPasswordUrl", resetPasswordUrl + link);
        emailInfo.put("ip", ip);
        emailInfo.put("date", simpleDateFormat.format(new Date()));
        emailUtil.sendRichEmail(email, "重置密码", emailInfo, "resetPassword.html");
        log.trace("{} 用户的密码重置邮件已经发送", email);

        return new CustomResponse<>(HttpStatus.OK);
    }

    @Override
    public void sendUserConfirmEmail(String email) throws MessagingException {
        String token = redisUtil.saveAccountActivationInfo(email);

        Map<String, Object> map = new HashMap<>();
        map.put("accountActivationUrl", accountActivationUrl + token);
        emailUtil.sendRichEmail(email, "账户激活", map, "accountActivation.html");
    }
}
