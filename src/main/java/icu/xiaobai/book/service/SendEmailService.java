package icu.xiaobai.book.service;

import icu.xiaobai.book.entity.response.CustomResponse;

import jakarta.mail.MessagingException;

public interface SendEmailService {
    CustomResponse<String> sendResetPasswordEmail(String email, String ip) throws MessagingException;
    void sendUserConfirmEmail(String email) throws MessagingException;
}
