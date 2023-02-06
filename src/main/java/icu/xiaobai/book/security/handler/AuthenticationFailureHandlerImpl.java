package icu.xiaobai.book.security.handler;

import icu.xiaobai.book.entity.response.CustomHttpStatus;
import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    WebUtil webUtil;

    public AuthenticationFailureHandlerImpl(WebUtil webUtil) {
        this.webUtil = webUtil;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof LockedException) {
            // 账户锁定
            webUtil.okResponse(response, new CustomResponse<>(CustomHttpStatus.ACCOUNT_EXCEPTION));
        } else {
            // 登录失败，直接返回401
            webUtil.okResponse(response, new CustomResponse<>(HttpStatus.UNAUTHORIZED));
        }
    }
}
