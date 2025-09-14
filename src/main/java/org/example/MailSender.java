package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailSender {
    public static void sendMail(String toEmail, String subject, String body) throws Exception {
        String fromEmail = System.getenv("EMAIL_SENDER");
        String password = System.getenv("EMAIL_PASSWORD");
        
        if (fromEmail == null || password == null) {
            throw new Exception("Email credentials not configured! Please set EMAIL_SENDER and EMAIL_PASSWORD environment variables.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");

        Transport.send(message);
        System.out.println("✅ Mail sent to " + toEmail);
    }
}
