package com.knu.algo_hive.auth.service;

import com.knu.algo_hive.common.exception.ConflictException;
import com.knu.algo_hive.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String email, String code) throws MessagingException {
        MimeMessage message = this.verifyCodeTemplate(email, code);

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            throw new ConflictException(ErrorCode.FAIL_SEND_EMAIL);
        }
    }

    public MimeMessage verifyCodeTemplate(String email, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(email);
        helper.setSubject("[Algo Hive] 이메일 인증번호");
        helper.setText(buildVerificationEmail(code), true);

        return message;
    }

    public String buildVerificationEmail(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("</head>");
        sb.append("<body style=\"margin: 0; padding: 0; width: 100%; text-align: center; background-color: #f9f9f9; font-family: Arial, sans-serif;\">");
        sb.append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #f9f9f9;\">");
        sb.append("<tr>");
        sb.append("<td align=\"center\">");
        sb.append("<table role=\"presentation\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #ffffff; border-radius: 8px;\">");

        // 로고 이미지
        sb.append("<tr>");
        sb.append("<td align=\"center\" style=\"padding: 20px;\">");
        sb.append("<img src=\"https://algo.knu-soft.site/files/47f478c6-8a0d-48fe-87e2-2a770847cfcb_image.png\" width=\"250\" style=\"display: block;\">");
        sb.append("</td>");
        sb.append("</tr>");

        // 제목
        sb.append("<tr>");
        sb.append("<td align=\"center\" style=\"background-color: #0076BF; padding: 20px; color: white; font-size: 24px; font-weight: bold;\">본인확인 인증 코드</td>");
        sb.append("</tr>");

        // 본문 내용
        sb.append("<tr>");
        sb.append("<td style=\"padding: 30px; text-align: left; font-size: 16px; color: #333333;\">");
        sb.append("<p style=\"margin: 0 0 10px 0;\">안녕하세요,</p>");
        sb.append("<p style=\"margin: 0 0 10px 0;\">본인 확인을 위해 아래 인증 코드를 입력해주세요:</p>");
        sb.append("<p style=\"display: block; font-size: 28px; font-weight: bold; text-align: center; color: #555555; background: #f1f1f1; padding: 15px; border-radius: 4px;\">").append(code).append("</p>");
        sb.append("<p style=\"margin: 20px 0 0 0;\">인증 코드는 <strong>3분</strong> 동안만 유효합니다.</p>");
        sb.append("</td>");
        sb.append("</tr>");

        // 푸터
        sb.append("<tr>");
        sb.append("<td align=\"center\" style=\"font-size: 12px; color: #888888; padding: 20px;\">이 이메일은 자동으로 발송되었습니다. 문의가 필요하시면 ").append(senderEmail).append(" 으로 연락해주세요.</td>");
        sb.append("</tr>");

        sb.append("</table>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }


}
