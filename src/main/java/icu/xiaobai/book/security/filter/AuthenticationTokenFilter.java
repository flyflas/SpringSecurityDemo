package icu.xiaobai.book.security.filter;

import icu.xiaobai.book.entity.UserInfo;
import icu.xiaobai.book.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Objects;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    RedisUtil redisUtil;

    public AuthenticationTokenFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");

        // 请求并未携带任何的Token
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 请求携带了Token
        UserInfo userInfo = redisUtil.readUserInfo(token);

        // Token 信息过期
        if (Objects.isNull(userInfo)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 将验证信息放入到Context中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userInfo, token, null);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
