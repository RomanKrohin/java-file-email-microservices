package com.example.mailservice;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {


    @Autowired
    private JavaMailSender mailSender;

    public void receiveUploadMessage(String message) {
        String[] parts = message.split(":", 2);
        String email = parts[0];
        String content = parts[1];
        sendEmail(email, "Upload Notification", content);
    }

    public void receiveDownloadMessage(String message) {
        String[] parts = message.split(":", 2);
        String email = parts[0];
        String content = parts[1];
        sendEmail(email, "Download Notification", content);
    }

    public void receiveAuthMessage(String message) {
        String[] parts = message.split(":", 2);
        String email = parts[0];
        String content = parts[1];
        sendEmail(email, "Auth Notification", content);
    }

    public void sendEmail(String to, String subject, String text) {
        System.out.println(text);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(text);
        };

        mailSender.send(messagePreparator);
    }
}
