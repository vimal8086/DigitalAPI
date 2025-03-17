package com.one.digitalapi.service;

import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.repository.BusRepository;
import com.one.digitalapi.repository.ReservationRepository;
import com.one.digitalapi.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public Reservations addReservation(ReservationDTO reservationDTO) throws ReservationException, LoginException {

        // Fetch User Detail using only userId
        User user = userRepository.findByUserId(reservationDTO.getUserId())
                .orElseThrow(() -> new LoginException("User Not Found for ID: " + reservationDTO.getUserId()));

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
        reservation.setUser(user);
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
        Reservations existingReservation = reservationRepository.findById(reservation.getReservationId())
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservation.getReservationId()));

        existingReservation.setSource(reservation.getSource());
        existingReservation.setDestination(reservation.getDestination());
        existingReservation.setJourneyDate(reservation.getJourneyDate());
        existingReservation.setNoOfSeatsBooked(reservation.getNoOfSeatsBooked());
        existingReservation.setReservationStatus(reservation.getReservationStatus());

        return reservationRepository.save(existingReservation);
    }

    @Override
    public Reservations deleteReservation(Integer reservationId, String cancellationReason) throws ReservationException, LoginException {
        Reservations existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservationId));

        // Refund Calculation Logic
        Integer refundAmount = calculateRefund(existingReservation);

        // Update Cancellation Details
        existingReservation.setReservationStatus("CANCELLED");
        existingReservation.setCancellationReason(cancellationReason);
        existingReservation.setRefundAmount(refundAmount);

        // Update bus seat availability
        Bus bus = existingReservation.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + existingReservation.getNoOfSeatsBooked());
        busRepository.save(bus);

        return reservationRepository.save(existingReservation);
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

    /**
     * Refund calculation based on cancellation timing
     */
    private Integer calculateRefund(Reservations reservation) {
        LocalDate today = LocalDate.now();
        LocalDate journeyDate = reservation.getJourneyDate();
        Integer fare = reservation.getFare();

        if (today.isEqual(journeyDate) || today.isAfter(journeyDate)) {
            return 0;  // No refund on the same day or after journey date
        } else if (today.plusDays(1).isEqual(journeyDate)) {
            return (int) (fare * 0.50); // 50% refund if canceled one day before
        } else if (today.plusDays(3).isAfter(journeyDate)) {
            return (int) (fare * 0.75); // 75% refund if canceled within 3 days
        } else {
            return fare; // Full refund if canceled more than 3 days in advance
        }
    }

}