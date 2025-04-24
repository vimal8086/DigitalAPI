package com.one.digitalapi.service;

import com.one.digitalapi.config.ReservationProperties;
import com.one.digitalapi.dto.BookedSeatDTO;
import com.one.digitalapi.dto.BusDTO;
import com.one.digitalapi.dto.PassengerDTO;
import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.*;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.logger.DefaultLogger;
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

    private static final String CLASSNAME = "ReservationServiceImpl";

    private static final DefaultLogger LOGGER = new DefaultLogger(ReservationServiceImpl.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReservationProperties reservationProperties;

    @Override
    public Reservations addReservation(ReservationDTO reservationDTO, String discountCode) {
        LOGGER.infoLog(CLASSNAME, "addReservation", "Entering addReservation method");

        if (reservationDTO == null) {
            LOGGER.errorLog(CLASSNAME, "addReservation", "ReservationDTO is null");
            throw new IllegalArgumentException("ReservationDTO cannot be null");
        }

        String userId = reservationDTO.getUserId();
        if (userId == null || userId.isBlank()) {
            LOGGER.errorLog(CLASSNAME, "addReservation", "User ID is missing");
            throw new LoginException("User ID cannot be null or empty");
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    LOGGER.errorLog(CLASSNAME, "addReservation", "User not found: " + userId);
                    return new LoginException("User Not Found for ID: " + userId);
                });

        BusDTO busDTO = reservationDTO.getBusDTO();
        if (busDTO == null || busDTO.getBusId() == null) {
            LOGGER.errorLog(CLASSNAME, "addReservation", "Bus information missing");
            throw new ReservationException("Bus information is missing or incomplete");
        }

        Bus bus = busRepository.findById(busDTO.getBusId())
                .orElseThrow(() -> {
                    LOGGER.errorLog(CLASSNAME, "addReservation", "Bus not found for ID: " + busDTO.getBusId());
                    return new ReservationException("Bus not found for ID: " + busDTO.getBusId());
                });

        int noOfSeats = reservationDTO.getNoOfSeatsToBook() != null ? reservationDTO.getNoOfSeatsToBook() : 0;
        if (noOfSeats <= 0) {
            LOGGER.errorLog(CLASSNAME, "addReservation", "Invalid number of seats: " + noOfSeats);
            throw new ReservationException("Number of seats to book must be greater than 0");
        }

        LOGGER.debugLog(CLASSNAME, "addReservation", "Creating reservation entity");

        Reservations reservation = new Reservations();
        reservation.setSource(reservationDTO.getSource());
        reservation.setDestination(reservationDTO.getDestination());
        reservation.setNoOfSeatsBooked(noOfSeats);
        reservation.setJourneyDate(reservationDTO.getJourneyDate());
        reservation.setBus(bus);
        reservation.setUser(user);

        int baseFare = bus.getFarePerSeat() * noOfSeats;
        double discountAmount = 0.0;

        if (discountCode != null && !discountCode.isBlank()) {
            LOGGER.debugLog(CLASSNAME, "addReservation", "Applying discount code: " + discountCode);
            discountRepository.findByCode(discountCode).ifPresent(discount -> {
                if (isDiscountValid(discount)) {
                    reservation.setDiscount(discount);
                    double discounted = calculateDiscountAmount((double) baseFare, discount);
                    reservation.setDiscountAmount(discounted); // Actual amount reduced
                } else {
                    LOGGER.warnLog(CLASSNAME, "addReservation", "Discount code is invalid or expired");
                }
            });
        }

        discountAmount = reservation.getDiscountAmount() != null ? reservation.getDiscountAmount() : 0.0;
        double discountedFare = baseFare - discountAmount;

        double gstAmount = discountedFare * (reservationProperties.getGstPercentage() / 100.0);
        double totalAmount = discountedFare + gstAmount;

        // Set updated values in reservation
        reservation.setFare(baseFare); // original fare
        reservation.setGstAmount(gstAmount);
        reservation.setTotalAmount(totalAmount);

        reservation.setOrderId(reservationDTO.getOrderId()); // You can customize this format
        reservation.setReservationStatus(DigitalAPIConstant.CONFIRMED);
        reservation.setReservationType(DigitalAPIConstant.ONLINE);

        reservation.setUsername(reservationDTO.getUsername());
        reservation.setMobileNumber(reservationDTO.getMobileNumber());
        reservation.setEmail(reservationDTO.getEmail());
        reservation.setGender(reservationDTO.getGender());

        reservation.setPickupAddress(reservationDTO.getPickupAddress());
        reservation.setPickupTime(reservationDTO.getPickupTime());
        reservation.setDropAddress(reservationDTO.getDropAddress());
        reservation.setDropTime(reservationDTO.getDropTime());

        List<PassengerDTO> passengerDTOs = reservationDTO.getPassengers();
        if (passengerDTOs == null || passengerDTOs.isEmpty()) {
            LOGGER.errorLog(CLASSNAME, "addReservation", "Passenger list is empty");
            throw new ReservationException("At least one passenger must be provided");
        }

        List<Passenger> passengerList = passengerDTOs.stream()
                .filter(Objects::nonNull)
                .map(passengerDTO -> {
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

        reservation.setReservationDate(LocalDateTime.now().plusSeconds(2));

        LOGGER.debugLog(CLASSNAME, "addReservation", "Saving reservation");
        Reservations savedReservation = reservationRepository.save(reservation);
        LOGGER.infoLog(CLASSNAME, "addReservation", "Reservation saved with ID: " + savedReservation.getReservationId());

        try {

            byte[] pdfBytes = pdfService.generateFormattedTicket(savedReservation.getReservationId());

            String fileName = "Bus_Ticket_" + savedReservation.getReservationId() + ".pdf";

            emailService.sendTicketEmail(savedReservation.getEmail(), pdfBytes, fileName, savedReservation.getSource()
                    + " to " + savedReservation.getDestination() + " on " + savedReservation.getJourneyDate());


        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, "addReservation", "Error sending ticket email: " + e.getMessage());
        }

        return savedReservation;
    }



    @Override
    @Transactional
    public Reservations deleteReservation(Integer reservationId, String cancellationReason) {
        LOGGER.infoLog(CLASSNAME, "deleteReservation", "Deleting reservation ID: " + reservationId);

        Reservations existingReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    LOGGER.errorLog(CLASSNAME, "deleteReservation", "Reservation not found: " + reservationId);
                    return new ReservationException("Reservation not found for ID: " + reservationId);
                });

        if (DigitalAPIConstant.CANCELLED.equals(existingReservation.getReservationStatus())) {
            LOGGER.warnLog(CLASSNAME, "deleteReservation", "Reservation already cancelled");
            throw new ReservationException("Reservation Already CANCELLED");
        }

        Integer refundAmount = calculateRefund(existingReservation);
        LOGGER.infoLog(CLASSNAME, "deleteReservation", "Calculated refund amount: " + refundAmount);

        Bus bus = existingReservation.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + existingReservation.getNoOfSeatsBooked());
        busRepository.save(bus);
        LOGGER.debugLog(CLASSNAME, "deleteReservation", "Updated bus seat availability");

        reservationRepository.updateReservationStatus(reservationId, DigitalAPIConstant.CANCELLED, cancellationReason, refundAmount);
        LOGGER.infoLog(CLASSNAME, "deleteReservation", "Reservation status updated in DB");

        existingReservation.setRefundAmount(refundAmount);
        existingReservation.setReservationStatus(DigitalAPIConstant.CANCELLED);
        existingReservation.setCancellationReason(cancellationReason);

        return existingReservation;
    }

    @Override
    public Reservations viewAllReservation(Integer reservationId) {
        LOGGER.infoLog(CLASSNAME, "viewAllReservation", "Fetching reservation ID: " + reservationId);
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    LOGGER.errorLog(CLASSNAME, "viewAllReservation", "Reservation not found: " + reservationId);
                    return new ReservationException("Reservation not found for ID: " + reservationId);
                });
    }

    @Override
    public List<Reservations> getReservationDeatials() {
        LOGGER.infoLog(CLASSNAME, "getReservationDeatials", "Fetching all reservations");
        List<Reservations> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            LOGGER.warnLog(CLASSNAME, "getReservationDeatials", "No reservations found");
            throw new ReservationException("No reservations found!");
        }
        return reservations;
    }

    @Override
    public List<String> getBookedSeatsForBus(Integer busId, LocalDateTime journeyStart, LocalDateTime journeyEnd) {
        LOGGER.infoLog(CLASSNAME, "getBookedSeatsForBus", "Fetching booked seats for Bus ID: " + busId);
        List<Reservations> reservations = reservationRepository
                .findByBus_BusIdAndReservationStatusAndJourneyDateBetween(busId, "CONFIRMED", journeyStart, journeyEnd);

        if (reservations.isEmpty()) {
            LOGGER.warnLog(CLASSNAME, "getBookedSeatsForBus", "No reservations found for bus ID: " + busId);
            return Collections.emptyList();
        }

        return reservations.stream()
                .flatMap(reservation -> reservation.getPassengers().stream())
                .map(Passenger::getSeatName)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookedSeatDTO> getAllBookedSeats() {
        LOGGER.infoLog(CLASSNAME, "getAllBookedSeats", "Fetching all future booked seats");
        LocalDateTime now = LocalDateTime.now();
        List<Reservations> reservations = reservationRepository.findByReservationStatusAndJourneyDateAfter("CONFIRMED", now);

        if (reservations.isEmpty()) {
            LOGGER.warnLog(CLASSNAME, "getAllBookedSeats", "No booked seats found");
            return new ArrayList<>();
        }

        Map<Integer, List<String>> groupedSeats = reservations.stream()
                .flatMap(reservation -> reservation.getPassengers().stream()
                        .map(passenger -> new AbstractMap.SimpleEntry<>(reservation.getBus().getBusId(), passenger.getSeatName())))
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())));

        return groupedSeats.entrySet().stream()
                .map(entry -> new BookedSeatDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Reservations getReservationById(Integer id) {
        LOGGER.infoLog(CLASSNAME, "getReservationById", "Fetching reservation by ID: " + id);
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reservations> getReservationsByUserId(String userId) {
        LOGGER.infoLog(CLASSNAME, "getReservationsByUserId", "Fetching reservations for user: " + userId);
        return reservationRepository.findByUser_UserIdOrderByReservationDateDesc(userId);
    }

    private Integer calculateRefund(Reservations reservation) {
        LOGGER.debugLog(CLASSNAME, "calculateRefund", "Calculating refund");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime journeyDateTime = reservation.getJourneyDate();
        Integer fare = reservation.getFare();

        if (journeyDateTime.isAfter(now) || journeyDateTime.minusHours(1).isAfter(now)) {
            return fare;
        }
        return 0;
    }

    private boolean isDiscountValid(Discount discount) {
        LocalDateTime now = LocalDateTime.now();
        return (discount.getStartDate().isBefore(now) || discount.getStartDate().isEqual(now)) &&
                discount.getEndDate().isAfter(now);
    }

    private Double calculateDiscountAmount(Double originalFare, Discount discount) {
        if (discount.getType() == DiscountType.PERCENTAGE) {
            return (originalFare * discount.getValue() / 100);
        } else if (discount.getType() == DiscountType.FLAT) {
            return Math.min(originalFare, discount.getValue()); // avoid negative fare
        }
        return 0.0;
    }

}