package com.one.digitalapi.service;

import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.entity.Passenger;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.repository.BusRepository;
import com.one.digitalapi.repository.ReservationRepository;
import com.one.digitalapi.repository.UserRepository;
import com.one.digitalapi.utils.DigitalAPIConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
        reservation.setReservationStatus(DigitalAPIConstant.CONFIRMED);
        reservation.setReservationType(DigitalAPIConstant.ONLINE);

        // Set new fields
        reservation.setUsername(reservationDTO.getUsername());
        reservation.setMobileNumber(reservationDTO.getMobileNumber());
        reservation.setEmail(reservationDTO.getEmail());
        reservation.setGender(reservationDTO.getGender());

        // Set passengers
        List<Passenger> passengerList = reservationDTO.getPassengers().stream().map(passengerDTO -> {
            Passenger passenger = new Passenger();
            passenger.setName(passengerDTO.getName());
            passenger.setEmail(passengerDTO.getEmail());
            passenger.setAge(passengerDTO.getAge());
            passenger.setGender(passengerDTO.getGender());
            passenger.setContact(passengerDTO.getContact());
            passenger.setReservation(reservation);
            return passenger;
        }).collect(Collectors.toList());

        reservation.setPassengers(passengerList);


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
    public Reservations deleteReservation(Integer reservationId, String cancellationReason) throws ReservationException {
        Reservations existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservationId));

        // Calculate refund based on the cancellation time
        Integer refundAmount = calculateRefund(existingReservation);

        // Update Cancellation Details
        existingReservation.setReservationStatus(DigitalAPIConstant.CANCELLED);
        existingReservation.setCancellationReason(cancellationReason);
        existingReservation.setRefundAmount(refundAmount);

        // Update bus seat availability
        Bus bus = existingReservation.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + existingReservation.getNoOfSeatsBooked());
        busRepository.save(bus);

        reservationRepository.save(existingReservation);

        // ✅ Set custom message inside an unused field (e.g., `cancellationReason`)
        existingReservation.setCancellationReason("Reservation cancelled successfully. Refund Amount: " + refundAmount);

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

    /**
     * Refund calculation based on cancellation timing
     */
    private Integer calculateRefund(Reservations reservation) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        LocalDate journeyDate = reservation.getJourneyDate();
        LocalTime journeyTime = reservation.getReservationTime(); // Assuming this is the journey time

        Integer fare = reservation.getFare();

        // Case 1: If the journey date is in the future, check the 1-hour rule
        if (journeyDate.isAfter(today)) {
            return fare;  // Full refund for future journeys
        }

        // Case 2: If canceling on the same day, check if it's at least 1 hour before journey time
        if (journeyDate.isEqual(today) && now.isBefore(journeyTime.minusHours(1))) {
            return fare;  // Full refund
        }

        // Case 3: If canceling within 1 hour of journey time or after the journey date, no refund
        return 0;
    }

}