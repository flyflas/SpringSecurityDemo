package icu.xiaobai.book.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import icu.xiaobai.book.entity.User;
import icu.xiaobai.book.entity.UserInfo;
import icu.xiaobai.book.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, username);
        User user = userMapper.selectOne(wrapper);

        // 用户不存在
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("该用户不存在");
        }

        return new UserInfo(user);
    }
}
