package com.notification.helper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class EmailSenderHelper {

    @Value("${spring.mail.username}")
    private String fromEmailAddress;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Asynchronously sends an email using the provided content to the specified recipient.
     *
     * @param recipient The email address of the recipient.
     * @param subject   The subject of the email.
     * @param content   The content of the email.
     * @throws UnsupportedEncodingException If an unsupported encoding is encountered.
     * @throws MessagingException           If an error occurs while sending the email message.
     */
    @Async
    public void sendEmail(String recipient, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromEmailAddress);
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}
