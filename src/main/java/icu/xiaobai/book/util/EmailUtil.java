package icu.xiaobai.book.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailUtil {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public EmailUtil(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /* 简单文本邮件发送函数 */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }

    /**
     * 富文本邮件发送函数
     * @param to    待接收邮件地址
     * @param subject   邮件主题
     * @param values    thymeleaf 需要填充的值
     * @param template  thymeleaf 模板名称
     * @throws MessagingException 模板异常
     */
    public void sendRichEmail(String to, String subject, Map<String, Object> values, String template) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        // 渲染网页
        Context context = new Context();
        context.setVariables(values);
        String content = templateEngine.process(template, context);

        mimeMessageHelper.setText(content, true);
        javaMailSender.send(message);
    }
}
