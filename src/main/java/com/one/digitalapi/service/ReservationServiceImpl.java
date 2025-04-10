package com.one.digitalapi.service;

import com.one.digitalapi.dto.BookedSeatDTO;
import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.*;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.repository.BusRepository;
import com.one.digitalapi.repository.DiscountRepository;
import com.one.digitalapi.repository.ReservationRepository;
import com.one.digitalapi.repository.UserRepository;
import com.one.digitalapi.utils.DigitalAPIConstant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Reservations addReservation(ReservationDTO reservationDTO, String discountCode) {

        // Fetch User Detail using only userId
        User user = userRepository.findByUserId(reservationDTO.getUserId()).
                orElseThrow(() -> new LoginException("User Not Found for ID: " + reservationDTO.getUserId()));


        // Fetch bus details
        Bus bus = busRepository.findById(reservationDTO.getBusDTO().getBusId())
                .orElseThrow(() -> new ReservationException("Bus not found for ID: " + reservationDTO.getBusDTO().getBusId()));


        // Create new reservation
        Reservations reservation = new Reservations();
        reservation.setSource(reservationDTO.getSource());
        reservation.setDestination(reservationDTO.getDestination());
        reservation.setNoOfSeatsBooked(reservationDTO.getNoOfSeatsToBook());
        reservation.setJourneyDate(reservationDTO.getJourneyDate());
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

        //Pickup and drop fields
        reservation.setPickupAddress(reservationDTO.getPickupAddress());
        reservation.setPickupTime(reservationDTO.getPickupTime());
        reservation.setDropAddress(reservationDTO.getDropAddress());
        reservation.setDropTime(reservationDTO.getDropTime());


        // Set passengers
        List<Passenger> passengerList = reservationDTO.getPassengers().stream().map(passengerDTO -> {
            Passenger passenger = new Passenger();
            passenger.setName(passengerDTO.getName());
            passenger.setEmail(passengerDTO.getEmail());
            passenger.setAge(passengerDTO.getAge());
            passenger.setGender(passengerDTO.getGender());
            passenger.setContact(passengerDTO.getContact());
            passenger.setSeatName(passengerDTO.getSeatName());
            passenger.setReservation(reservation);
            return passenger;
        }).collect(Collectors.toList());

        reservation.setPassengers(passengerList);

        // For Discount
        if (discountCode != null && !discountCode.isEmpty()) {
            Optional<Discount> discountOpt = discountRepository.findByCode(discountCode);
            if (discountOpt.isPresent()) {
                Discount discount = discountOpt.get();
                if (isDiscountValid(discount)) {
                    reservation.setDiscount(discount);
                    Integer discountedFare = calculateDiscountedFare(reservation.getFare(), discount);
                    reservation.setFare(discountedFare);
                }
            }
        }

        // Adding Here For Updated Reservation Date Value
        reservation.setReservationDate(LocalDateTime.now().plusSeconds(2));

        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservations deleteReservation(Integer reservationId, String cancellationReason) {
        Reservations existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservationId));

        // Check if reservation is already cancelled
        if (DigitalAPIConstant.CANCELLED.equals(existingReservation.getReservationStatus())) {
            throw new ReservationException("Reservation Already CANCELLED");
        }

        // Calculate refund based on the cancellation time
        Integer refundAmount = calculateRefund(existingReservation);

        // Update bus seat availability
        Bus bus = existingReservation.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + existingReservation.getNoOfSeatsBooked());
        busRepository.save(bus);

        // Update Reservation Status Directly in DB
        reservationRepository.updateReservationStatus(reservationId, DigitalAPIConstant.CANCELLED, cancellationReason, refundAmount);

        // Manually set refundAmount and status before returning
        existingReservation.setRefundAmount(refundAmount);
        existingReservation.setReservationStatus(DigitalAPIConstant.CANCELLED);
        existingReservation.setCancellationReason(cancellationReason);

        return existingReservation;  // Returns updated object with manually set fields
    }

    @Override
    public Reservations viewAllReservation(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found for ID: " + reservationId));
    }

    @Override
    public List<Reservations> getReservationDeatials() {
        List<Reservations> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            throw new ReservationException("No reservations found!");
        }
        return reservations;
    }

    @Override
    public List<String> getBookedSeatsForBus(Integer busId, LocalDateTime journeyStart, LocalDateTime journeyEnd)
            throws ReservationException {

        // Fetch all confirmed reservations within the date range
        List<Reservations> reservations = reservationRepository
                .findByBus_BusIdAndReservationStatusAndJourneyDateBetween(busId, "CONFIRMED", journeyStart, journeyEnd);

        if (reservations.isEmpty()) {
            return Collections.emptyList(); // Return empty list instead of throwing exception
        }

        // Collect all booked seat names
        return reservations.stream()
                .flatMap(reservation -> reservation.getPassengers().stream())
                .map(Passenger::getSeatName)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookedSeatDTO> getAllBookedSeats() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Fetch all confirmed reservations with a journeyDate >= today
        List<Reservations> reservations = reservationRepository.findByReservationStatusAndJourneyDateAfter("CONFIRMED", now);

        // If no reservations are found, throw an exception or return an empty list
        if (reservations.isEmpty()) {
            return new ArrayList<>(); // Return an empty list if no reservations are found
        }

        // Collect all booked seats with busId and seatName
        Map<Integer, List<String>> groupedSeats = reservations.stream()
                .flatMap(reservation -> reservation.getPassengers().stream() // Stream passengers from each reservation
                        .map(passenger -> new AbstractMap.SimpleEntry<>(reservation.getBus().getBusId(), passenger.getSeatName()))) // Pair busId with seatName
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty()) // Filter out empty seat names
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, // Group by busId
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList()))); // Collect seat names into a list

        // Convert the grouped data into the required BookedSeatDTO format
        List<BookedSeatDTO> bookedSeatDTOList = groupedSeats.entrySet().stream()
                .map(entry -> new BookedSeatDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return bookedSeatDTOList;
    }

    @Override
    public Reservations getReservationById(Integer id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reservations> getReservationsByUserId(String userId) {
        return reservationRepository.findByUser_UserIdOrderByReservationDateDesc(userId);
    }


    /**
     * Refund calculation based on cancellation timing
     */
    private Integer calculateRefund(Reservations reservation) {
        LocalDateTime now = LocalDateTime.now(); // Get current date and time
        LocalDateTime journeyDateTime = reservation.getJourneyDate(); // Get journey date & time
        Integer fare = reservation.getFare();

        // Case 1: If the journey is in the future, full refund
        if (journeyDateTime.isAfter(now)) {
            return fare;  // Full refund for future journeys
        }

        // Case 2: If canceling at least 1 hour before journey time, full refund
        if (journeyDateTime.minusHours(1).isAfter(now)) {
            return fare;  // Full refund
        }

        // Case 3: If canceling within 1 hour of journey time or after the journey date, no refund
        return 0;
    }

    // For Discount
    private boolean isDiscountValid(Discount discount) {
        LocalDateTime now = LocalDateTime.now();

        return (discount.getStartDate().isBefore(now) || discount.getStartDate().isEqual(now)) &&
                (discount.getEndDate().isAfter(now));
    }

    private Integer calculateDiscountedFare(Integer originalFare, Discount discount) {
        if (discount.getType() == DiscountType.PERCENTAGE) {
            return originalFare - (originalFare * discount.getValue() / 100);
        } else if (discount.getType() == DiscountType.FLAT) {
            return originalFare - discount.getValue();
        }
        return originalFare;
    }

}