package icu.xiaobai.book.security.handler;

import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final WebUtil webUtil;

    public AccessDeniedHandlerImpl(WebUtil webUtil) {
        this.webUtil = webUtil;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        webUtil.okResponse(response, new CustomResponse<>(HttpStatus.FORBIDDEN));
    }
}
