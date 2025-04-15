package com.one.digitalapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTicketEmail(String toEmail, byte[] pdfBytes, String fileName, String subject) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Bus Ticket - " + subject);
        helper.setText("Dear Customer,\n\nThank you for booking with Orange Motions. Please find your ticket attached.\n\nSafe Travels!", false);

        InputStreamSource attachment = new ByteArrayResource(pdfBytes);
        helper.addAttachment(fileName, attachment);

        mailSender.send(message);
    }
}