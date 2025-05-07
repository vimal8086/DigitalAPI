package com.one.digitalapi.service;

import com.one.digitalapi.dto.BookedSeatDTO;
import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Reservations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReservationService {

    public Reservations addReservation(ReservationDTO reservationDTO, String discountCode);

    public Reservations deleteReservation(Integer reservationId, String cancellationReason);

    public Reservations viewAllReservation(Integer reservationId);

    public List<Reservations> getReservationDetails();

    public List<String> getBookedSeatsForBus(Integer busId,  LocalDateTime journeyStart, LocalDateTime journeyEnd);

    public List<BookedSeatDTO> getAllBookedSeats();

    public List<Reservations> getReservationsByUserId(String userId);

    public Map<String, String> resendTicket(Integer reservationId);
}

