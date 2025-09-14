package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailSender {
    public static void sendMail(String toEmail, String subject, String body, 
                               String fromEmail, String password, 
                               String smtpHost, String smtpPort, 
                               boolean enableSSL, boolean enableSTARTTLS) throws Exception {
        
        if (fromEmail == null || password == null || fromEmail.trim().isEmpty() || password.trim().isEmpty()) {
            throw new Exception("Email credentials not configured! Please configure email settings in the Settings tab.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        
        // Configure SSL/TLS settings
        if (enableSTARTTLS) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
        }
        if (enableSSL && !enableSTARTTLS) {
            // Only use SSL if STARTTLS is not enabled (they conflict)
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        
        // Additional Gmail-specific settings
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", smtpHost != null ? smtpHost : "smtp.gmail.com");
        
        props.put("mail.smtp.host", smtpHost != null ? smtpHost : "smtp.gmail.com");
        props.put("mail.smtp.port", smtpPort != null ? smtpPort : "587");

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
    
    // Backward compatibility method - deprecated
    @Deprecated
    public static void sendMail(String toEmail, String subject, String body) throws Exception {
        throw new Exception("This method is deprecated. Please use the new sendMail method with email settings parameters.");
    }
}
