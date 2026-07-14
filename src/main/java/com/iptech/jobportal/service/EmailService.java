// package com.iptech.jobportal.service;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender mailSender;

//     @Value("${app.mail.from}")
//     private String fromEmail;

//     // Purana plain text wala (OTP jaise chhote messages ke liye rehne do)
//     public void sendEmail(String to, String subject, String text) {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
//             helper.setFrom(fromEmail);
//             helper.setTo(to);
//             helper.setSubject(subject);
//             helper.setText(text, false);
//             mailSender.send(message);
//         } catch (MessagingException e) {
//             throw new RuntimeException("Failed to send email", e);
//         }
//     }

//     // Naya — HTML email bhejne ke liye
//     public void sendHtmlEmail(String to, String subject, String htmlContent) {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//             helper.setFrom(fromEmail);
//             helper.setTo(to);
//             helper.setSubject(subject);
//             helper.setText(htmlContent, true); // true = HTML
//             mailSender.send(message);
//         } catch (MessagingException e) {
//             throw new RuntimeException("Failed to send email", e);
//         }
//     }

//     public String generateOtp() {
//         int otp = (int) (Math.random() * 900000) + 100000;
//         return String.valueOf(otp);
//     }
// }

package com.iptech.jobportal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String fromEmail;

    @Value("${brevo.sender.name}")
    private String fromName;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    // Plain text wala (OTP jaise chhote messages ke liye)
    public void sendEmail(String to, String subject, String text) {
        sendViaApi(to, subject, text);
    }

    // HTML email bhejne ke liye
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        sendViaApi(to, subject, htmlContent);
    }

    private void sendViaApi(String to, String subject, String content) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> sender = new HashMap<>();
            sender.put("email", fromEmail);
            sender.put("name", fromName);

            Map<String, Object> recipient = new HashMap<>();
            recipient.put("email", to);

            Map<String, Object> body = new HashMap<>();
            body.put("sender", sender);
            body.put("to", List.of(recipient));
            body.put("subject", subject);
            body.put("htmlContent", content);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(BREVO_URL, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via Brevo API", e);
        }
    }

    public String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}