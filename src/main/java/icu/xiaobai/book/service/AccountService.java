package icu.xiaobai.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.xiaobai.book.entity.response.CustomResponse;
import icu.xiaobai.book.entity.User;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface AccountService {
    /* 用户密码重置函数 */
    CustomResponse<String> resetPassword(String key);
    CustomResponse<String> addUser(User user) throws MessagingException;
    CustomResponse<String> checkSignUpEmail(String key);
    CustomResponse<Map<String, String>> getAccountStatus();
    CustomResponse<String> activateAccount() throws MessagingException;
    boolean checkEmailUsed(String email);
    CustomResponse<String> getLimitedToken(String email, String password) throws JsonProcessingException;
}
