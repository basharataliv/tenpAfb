package com.afba.imageplus.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtility {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    private final Logger logger = LoggerFactory.getLogger(EmailUtility.class);

    public EmailUtility(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendHtmlMessage(String subject, String to, String cc, String bcc, String htmlBody, Integer priority) {

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(InternetAddress.parse(to));
            helper.setCc(InternetAddress.parse(cc));
            helper.setBcc(InternetAddress.parse(bcc));
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setPriority(priority);
            emailSender.send(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendTextMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
