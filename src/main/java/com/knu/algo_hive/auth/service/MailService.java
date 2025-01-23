package com.knu.algo_hive.auth.service;

import com.knu.algo_hive.common.exception.ConflictException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String email, String code) throws MessagingException {
        MimeMessage message = this.verifyCodeTemplate(email, code);

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            e.printStackTrace();
            throw new ConflictException("메일 발송 중 오류가 발생했습니다.");
        }
    }

    public MimeMessage verifyCodeTemplate(String email, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        StringBuilder sb = new StringBuilder();

        sb.append("<h2>안녕하세요.</h2>");
        sb.append("<h2>Algo-Hive 입니다.</h2>");
        sb.append("<br>");
        sb.append("<p>아래 인증코드를 회원가입 페이지에 입력해주세요</p>");
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<div align='center' style='border:1px solid black'>");
        sb.append("<h3 style='color:blue'>회원가입 인증코드 입니다</h3>");
        sb.append("<div style='font-size:130%'>");
        sb.append("<strong>").append(code).append("</strong></div><br/>");
        sb.append("</div>");

        String body = sb.toString();

        message.setSubject("[Algo Hive] 이메일 인증번호");
        message.setText(body, "UTF-8", "html");

        return message;
    }
}
