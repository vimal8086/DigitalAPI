package com.one.digitalapi.service;

import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationService {

    public Reservations addReservation(ReservationDTO reservationDTO) throws ReservationException, LoginException;

    public Reservations updateReservation(Reservations reservation) throws ReservationException, LoginException;

    public Reservations deleteReservation(Integer reservationId, String cancellationReason) throws ReservationException, LoginException;

    public Reservations viewAllReservation(Integer reservationId) throws LoginException;

    public List<Reservations> getReservationDeatials() throws ReservationException, LoginException;

}

