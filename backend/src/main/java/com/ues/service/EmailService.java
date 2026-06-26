package com.ues.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAccountApproved(String toEmail) {
        sendEmail(toEmail, "Account Approved - New Now UES",
                "Your account has been approved. You can now log in to New Now UES.");
    }

    public void sendAccountRejected(String toEmail, String reason) {
        String body = "Your account request has been rejected.";
        if (reason != null && !reason.isBlank()) {
            body += "\nReason: " + reason;
        }
        sendEmail(toEmail, "Account Request Rejected - New Now UES", body);
    }

    public void sendPasswordChanged(String toEmail) {
        sendEmail(toEmail, "Password Changed - New Now UES",
                "Your password has been changed successfully. If you did not make this change, please contact support.");
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Email sent to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
