package com.knu.algo_hive.auth.service;

import com.knu.algo_hive.common.exception.ConflictException;
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
            e.printStackTrace();
            throw new ConflictException("메일 발송 중 오류가 발생했습니다.");
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
        sb.append("<!doctype html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\" />");
        sb.append("<style>");
        sb.append(".element { width: 250px; height: auto;}");
        sb.append("* { -webkit-font-smoothing: antialiased; box-sizing: border-box; margin: 0; padding: 0; }");
        sb.append("body { margin: 0; padding: 0; width: 100%; text-align: center; background-color: #f9f9f9; font-family: 'Pretendard-Regular', Arial, sans-serif; }");
        sb.append("table { border-spacing: 0; margin: 0 auto; }");
        sb.append(".email-container { max-width: 600px; width: 100%; background-color: #ffffff; border-radius: 8px; overflow: hidden; }");
        sb.append(".header { background-color: #0076BF; padding: 20px; color: white; text-align: center; font-size: 24px; font-weight: bold; }");
        sb.append(".content { padding: 30px; text-align: left; font-size: 16px; color: #333333; }");
        sb.append(".code { display: block; margin: 20px 0; font-size: 28px; font-weight: bold; text-align: center; color: #555555; }");
        sb.append(".footer { text-align: center; font-size: 12px; color: #888888; padding: 20px; }");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table role=\"presentation\" class=\"email-container\">");
        sb.append("<td style=\"text-align: center; padding: 20px;\">");
        sb.append("<img class=\"element\" src=\"https://velog.velcdn.com/images/2iedo/post/b8d1a533-9531-429e-81e8-9b4733c78996/image.png\" />");
        sb.append("</td>");
        sb.append("<tr>");
        sb.append("<td class=\"header\">본인확인 인증 코드</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td class=\"content\">");
        sb.append("<p>안녕하세요,</p>");
        sb.append("<p>본인 확인을 위해 아래 인증 코드를 입력해주세요:</p>");
        sb.append("<span class=\"code\">").append(code).append("</span>");
        sb.append("<p>인증 코드는 <strong>3분</strong> 동안만 유효합니다.</p>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td class=\"footer\">이 이메일은 자동으로 발송되었습니다. 문의가 필요하시면 "+ senderEmail +"으로 연락해주세요.</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

}