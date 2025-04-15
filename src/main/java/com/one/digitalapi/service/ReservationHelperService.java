package com.one.digitalapi.service;

import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationHelperService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservations getReservationWithDetails(Integer reservationId) {
        // Load reservation with all required fields (users, passengers, etc.)
        return reservationRepository.findById(reservationId).orElse(null);
    }
}

