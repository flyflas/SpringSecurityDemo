package icu.xiaobai.book.controller.authentication;

import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.service.SendEmailService;
import icu.xiaobai.book.service.AccountService;
import icu.xiaobai.book.util.WebUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/reset")
@Slf4j
public class ResetController {
    private final SendEmailService sendEmailService;
    private final WebUtil webUtil;
    private final AccountService accountService;

    public ResetController(SendEmailService sendEmailService, WebUtil webUtil, AccountService accountService) {
        this.sendEmailService = sendEmailService;
        this.webUtil = webUtil;
        this.accountService = accountService;
    }

    /**
     * 密码重置函数
     *
     * @param email 需要重置密码的邮箱
     * @return
     *      200 完成重置
     *      500 内部服务器错误
     */
    @PostMapping("/password")
    public CustomResponse<String> sendResetPasswordEmail(String email, HttpServletRequest request) throws UnknownHostException, MessagingException {
        String ip = webUtil.getIpAddr(request);
        log.debug("{} \t申请了重置密码, ip: {}", email, ip);
        return sendEmailService.sendResetPasswordEmail(email, ip);
    }

    @PostMapping("/password/{key}")
    public CustomResponse<String> resetPassword(@PathVariable String key) {
        return accountService.resetPassword(key);
    }
}
