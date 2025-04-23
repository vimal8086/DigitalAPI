package com.one.digitalapi.service;

import com.one.digitalapi.entity.UserOTP;
import com.one.digitalapi.repository.UserOTPRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    @Autowired
    private UserOTPRepository userOTPRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email) {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit OTP
        UserOTP userOTP = new UserOTP();
        userOTP.setEmail(email);
        userOTP.setOtp(otp);
        userOTP.setExpiry(LocalDateTime.now().plusMinutes(10));
        userOTPRepository.save(userOTP);

        try {
            sendOtpEmail(email, otp);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    private void sendOtpEmail(String toEmail, String otp) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("orangemotions.com", "Orange Motions");
        helper.setTo(toEmail);
        helper.setSubject("Your password reset request");

        String emailBody = String.format("""
        <p>Hello,</p>
        <p>Your OTP for password reset is: <strong>%s</strong></p>
        <p>This OTP is valid for 10 minutes.</p>
        <br>
        <p>Regards,<br>Orange Motion Team</p>
        """, otp);

        helper.setText(emailBody, true); // Set `true` for HTML content

        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {

        Optional<UserOTP> otpEntry = userOTPRepository.findTopByEmailOrderByExpiryDesc(email);
        if (otpEntry.isPresent()) {
            UserOTP record = otpEntry.get();
            if (record.getOtp().equals(otp) && record.getExpiry().isAfter(LocalDateTime.now())) {
                record.setVerified(true);
                userOTPRepository.save(record);
                return true;
            }
        }
        return false;
    }

    public boolean isOtpVerified(String email) {
        Optional<UserOTP> otpEntry = userOTPRepository.findTopByEmailOrderByExpiryDesc(email);
        return otpEntry.map(UserOTP::isVerified).orElse(false);
    }
}