package com.one.digitalapi.service;

import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.logger.DefaultLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class AsyncService {

    private static final String CLASSNAME = "AsyncService";

    private static final DefaultLogger LOGGER = new DefaultLogger(AsyncService.class);

    private final EmailService emailService;

    public AsyncService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void sendTicketAsync(Reservations reservation, byte[] pdfBytes, String fileName) {
        try {
            emailService.sendTicketEmail(
                    reservation.getEmail(),
                    pdfBytes,
                    fileName,
                    reservation.getSource() + " to " + reservation.getDestination() + " on " + reservation.getJourneyDate()
            );
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, "sendTicketAsync", "Error sending ticket email: " + e.getMessage());
        }
    }
}