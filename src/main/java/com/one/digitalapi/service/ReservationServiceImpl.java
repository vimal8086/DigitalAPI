package com.one.digitalapi.service;

import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.repository.BusRepository;
import com.one.digitalapi.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BusRepository busRepository;


    @Override
    public Reservations addReservation(ReservationDTO reservationDTO) throws ReservationException, LoginException {
        // Fetch bus details
        Bus bus = busRepository.findById(reservationDTO.getBusDTO().getBusId())
                .orElseThrow(() -> new ReservationException("Bus not found for ID: " + reservationDTO.getBusDTO().getBusId()));

        // Check seat availability
        if (bus.getAvailableSeats() < reservationDTO.getNoOfSeatsToBook()) {
            throw new ReservationException("Not enough seats available for booking!");
        }

        // Create new reservation
        Reservations reservation = new Reservations();
        reservation.setSource(reservationDTO.getSource());
        reservation.setDestination(reservationDTO.getDestination());
        reservation.setNoOfSeatsBooked(reservationDTO.getNoOfSeatsToBook());
        reservation.setJourneyDate(reservationDTO.getJourneyDate());
        reservation.setReservationDate(LocalDate.now());
        reservation.setReservationTime(LocalTime.now());
        reservation.setBus(bus);
        reservation.setFare(bus.getFarePerSeat() * reservationDTO.getNoOfSeatsToBook());
        reservation.setReservationStatus("CONFIRMED");
        reservation.setReservationType("ONLINE");

        // Update bus seat availability
        bus.setAvailableSeats(bus.getAvailableSeats() - reservationDTO.getNoOfSeatsToBook());
        busRepository.save(bus);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservations updateReservation(Reservations reservation) throws ReservationException, LoginException {
        // Check if reservation exists
        Reservations existingReservation = reservationRepository.findById(reservation.getReservationId())
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservation.getReservationId()));

        // Update reservation details
        existingReservation.setSource(reservation.getSource());
        existingReservation.setDestination(reservation.getDestination());
        existingReservation.setJourneyDate(reservation.getJourneyDate());
        existingReservation.setNoOfSeatsBooked(reservation.getNoOfSeatsBooked());
        existingReservation.setReservationStatus(reservation.getReservationStatus());

        return reservationRepository.save(existingReservation);
    }

    @Override
    public Reservations deleteReservation(Integer reservationId) throws ReservationException, LoginException {
        Reservations existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservationId));

        reservationRepository.deleteById(reservationId);
        return existingReservation;
    }

    @Override
    public Reservations viewAllReservation(Integer reservationId) throws LoginException {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservationId));

    }

    @Override
    public List<Reservations> getReservationDeatials() throws ReservationException, LoginException {
        List<Reservations> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            throw new ReservationException("No reservations found!");
        }
        return reservations;
    }
}