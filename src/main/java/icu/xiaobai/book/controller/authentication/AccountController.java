package icu.xiaobai.book.controller.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.entity.User;
import icu.xiaobai.book.service.AccountService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;

    }

    @PostMapping("/signup")
    public CustomResponse<String> signUp(@RequestBody User user) throws MessagingException {
        return accountService.addUser(user);
    }

    @GetMapping("/signup/{key}")
    public CustomResponse<String> checkSignUpEmail(@PathVariable String key) {
        return accountService.checkSignUpEmail(key);
    }

    @GetMapping("/status")
    public CustomResponse<Map<String, String>> getAccountStatus() {
        return accountService.getAccountStatus();
    }

    @PostMapping("/activate")
    public CustomResponse<String> activateAccount() throws MessagingException {
        return accountService.activateAccount();
    }

    @PostMapping("/limitedToken")
    public CustomResponse<String> getLimitedToken(@RequestParam("username") String email, String password) throws JsonProcessingException {
        return accountService.getLimitedToken(email, password);
    }
}
