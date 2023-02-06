package icu.xiaobai.book.security.handler;

import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.entity.UserInfo;
import icu.xiaobai.book.util.RedisUtil;
import icu.xiaobai.book.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    WebUtil webUtil;
    RedisUtil redisUtil;

    public AuthenticationSuccessHandlerImpl(WebUtil webUtil, RedisUtil redisUtil) {
        this.webUtil = webUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        String token = redisUtil.saveUserInfo(userInfo);
        webUtil.okResponse(response, new CustomResponse<>(HttpStatus.OK, token));
    }
}
