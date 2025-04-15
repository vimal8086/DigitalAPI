package com.one.digitalapi.service;

import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


// Currently Not in Use : Reference for future enhancement
@Service
public class AsyncService {

    private static final String CLASSNAME = "AsyncService";

    private static final DefaultLogger LOGGER = new DefaultLogger(AsyncService.class);

    private final PdfService pdfService;
    private final EmailService emailService;
    private final ReservationRepository reservationRepository;


    public AsyncService(PdfService pdfService, EmailService emailService, ReservationRepository reservationRepository) {
        this.pdfService = pdfService;
        this.emailService = emailService;
        this.reservationRepository = reservationRepository;
    }


    @Async
    @Transactional
    public void sendTicketAsync(Integer reservationId) {
        try {
            // Fetch reservation eagerly inside a transaction
            Optional<Reservations> optionalReservation = reservationRepository.findById(reservationId);

            if (optionalReservation.isEmpty()) {
                LOGGER.errorLog(CLASSNAME, "sendTicketAsync", "Reservation not found with ID: " + reservationId);
                return;
            }

            Reservations reservation = optionalReservation.get();
            User user = reservation.getUser(); // assuming reservation has getUser()

            byte[] pdfBytes = pdfService.generateFormattedTicket(reservationId);

            String fileName = "Bus_Ticket_" + reservationId + ".pdf";
            String subject = reservation.getSource() + " to " + reservation.getDestination()
                    + " on " + reservation.getJourneyDate();

            emailService.sendTicketEmail(user.getEmail(), pdfBytes, fileName, subject);

        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, "sendTicketAsync", "Error sending ticket email: " + e.getMessage());
        }
    }
}