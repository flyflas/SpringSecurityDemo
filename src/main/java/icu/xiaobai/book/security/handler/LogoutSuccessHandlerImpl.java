package icu.xiaobai.book.security.handler;

import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.util.RedisUtil;
import icu.xiaobai.book.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    WebUtil webUtil;
    RedisUtil redisUtil;

    public LogoutSuccessHandlerImpl(WebUtil webUtil, RedisUtil redisUtil) {
        this.webUtil = webUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*
         * 用户登出逻辑
         *      1. 检查 request 中是否含有request。如果没有，则返回 400
         *      2. 检查该 Token 是否有效。如果无效，则返回 401
         *      3. 从 Redis 中删除用户数据
         */
        String token = request.getHeader("token");

        if (Objects.isNull(token)) {
            // Token 不存在，用户没有权限注销
            webUtil.okResponse(response, new CustomResponse<>(HttpStatus.BAD_REQUEST));
            return;
        }

        if (Objects.isNull(redisUtil.readUserInfo(token))) {
            webUtil.okResponse(response, new CustomResponse<>(HttpStatus.UNAUTHORIZED));
            return;
        }

        // 从 Redis 中删除用户信息
        redisUtil.clearUserInfo(token);
        webUtil.okResponse(response, new CustomResponse<>(HttpStatus.OK));
    }
}
