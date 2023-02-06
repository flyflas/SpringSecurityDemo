package icu.xiaobai.book.exception;

import icu.xiaobai.book.entity.response.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {
    /**
     * 邮件异常处理器
     * 主要用于处理由系统邮件发送服务引起的异常
     * @param e Controller 抛出的异常
     * @return 500： 邮件发送异常。
     */
    @ExceptionHandler(MailSendException.class)
    public CustomResponse<String> emailExceptionHandler(Exception e) {
        return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CustomResponse<String> generalExceptionHandler(Exception e) {
        return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
