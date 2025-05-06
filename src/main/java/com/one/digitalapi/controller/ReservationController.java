package com.one.digitalapi.controller;

import com.one.digitalapi.dto.BookedSeatDTO;
import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.BookingService;
import com.one.digitalapi.service.BusService;
import com.one.digitalapi.service.PdfService;
import com.one.digitalapi.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.one.digitalapi.exception.GlobalExceptionHandler.getMapResponseEntity;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation Management", description = "APIs for managing reservations")
public class ReservationController {

    private static final String CLASSNAME = "ReservationController";

    private static final DefaultLogger LOGGER = new DefaultLogger(ReservationController.class);

    private final ReservationService reservationService;

    private final BookingService bookingService;

    private final BusService busService;

    private final PdfService pdfService;


    public ReservationController(ReservationService reservationService, BookingService bookingService, BusService busService, PdfService pdfService) {
        this.reservationService = reservationService;
        this.bookingService = bookingService;
        this.busService = busService;
        this.pdfService = pdfService;
    }


    @GetMapping("/generate/{reservationId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Generate PDF for ticket", description = "Generate PDF for ticket")
    public ResponseEntity<?> generateTicket(@PathVariable Integer reservationId) {
        try {

            byte[] pdfBytes = pdfService.generateFormattedTicket(reservationId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_" + reservationId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (ReservationException e) {

            LOGGER.errorLog(CLASSNAME, "generateTicket", "Reservation not found: " + e.getMessage());

            Map<String, Object> response = new HashMap<>();

            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IOException e) {

            LOGGER.errorLog(CLASSNAME, "generateTicket", "PDF generation failed due to IO error: " + e.getMessage());

            Map<String, Object> response = new HashMap<>();

            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {

            LOGGER.errorLog(CLASSNAME, "generateTicket", "Unexpected error: " + e.getMessage());

            Map<String, Object> response = new HashMap<>();

            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);        }
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Add a new reservation", description = "Creates a new reservation")
    public ResponseEntity<Reservations> addReservation(@Valid @RequestBody ReservationDTO reservationDTO,
                                                       @RequestParam(value = "discountCode", required = false) String discountCode) {
        String methodName = "addReservation";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to add reservation: " + reservationDTO);

        Reservations reservation = reservationService.addReservation(reservationDTO, discountCode);

        LOGGER.infoLog(CLASSNAME, methodName, "Reservation added successfully: " + reservation);

        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get a reservation with reservation id", description = "Get a reservation with reservation id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<Reservations> viewReservation(@PathVariable Integer id) throws ReservationException, LoginException {

        String methodName = "viewReservation";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to view reservation with ID: " + id);

        Reservations reservation = reservationService.viewAllReservation(id);

        LOGGER.infoLog(CLASSNAME, methodName, "Reservation retrieved successfully: " + reservation);

        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get all reservations", description = "Get all reservations")
    @ApiResponse(responseCode = "200", description = "List of reservations retrieved successfully")
    public ResponseEntity<List<Reservations>> getAllReservations() throws ReservationException, LoginException {

        String methodName = "getAllReservations";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to retrieve all reservations");

        List<Reservations> reservations = reservationService.getReservationDetails();

        LOGGER.infoLog(CLASSNAME, methodName, "Reservations retrieved successfully: " + reservations);

        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/cancel/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Cancel a reservation", description = "Cancel a reservation and calculate refund")
    public ResponseEntity<Map<String, Object>> cancelReservation(
            @PathVariable Integer id,
            @RequestParam String cancellationReason) throws ReservationException {

        String methodName = "cancelReservation";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to cancel reservation with ID: " + id + " for reason: " + cancellationReason);

        try {
            Reservations canceledReservation = reservationService.deleteReservation(id, cancellationReason);

            LOGGER.infoLog(CLASSNAME, methodName, "Reservation canceled successfully: " + canceledReservation);

            Map<String, Object> response = new HashMap<>();

            response.put("message", "Reservation cancelled successfully");

            response.put("refund amount", canceledReservation.getRefundAmount());

            response.put("status", canceledReservation.getReservationStatus());

            return ResponseEntity.ok(response);

        } catch (ReservationException e) {
            // Handle already cancelled reservation case
            Map<String, Object> response = new HashMap<>();

            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Get List of All Seats (Booked + Cached Available) for a Bus on a Specific Date",
            description = "Fetches list of all seats, including booked, cached available, and cached intermediate seats for a specific journey date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all seats"),
            @ApiResponse(responseCode = "400", description = "Invalid bus ID or date"),
            @ApiResponse(responseCode = "404", description = "Bus not found or no reservations found")
    })
    @GetMapping("/bus/allSeats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllSeatsForBus(
            @RequestParam Integer busId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String journeyDate) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validate busId
            if (busId == null || busId <= 0) {

                response.put("error", "Invalid Bus ID. Must be a positive number.");

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Convert String to LocalDate and validate format
            LocalDate journeyDateStr;
            try {

                journeyDateStr = LocalDate.parse(journeyDate);

            } catch (DateTimeParseException e) {

                response.put("error", "Invalid date format. Please use yyyy-MM-dd (e.g., 2024-04-05).");

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }


            // Fetch bus
            Bus bus = busService.viewBus(busId);

            if (bus == null) {
                throw new ReservationException("Bus not found for ID: " + busId);
            }

            int totalSeats = bus.getSeats();

            // Convert LocalDate to LocalDateTime range
            LocalDateTime journeyStart = journeyDateStr.atStartOfDay();
            LocalDateTime journeyEnd = journeyDateStr.atTime(LocalTime.MAX);

            // Fetch booked seats
            List<String> bookedSeats = reservationService.getBookedSeatsForBus(busId, journeyStart, journeyEnd);

            // Fetch cached available seats
            List<String> cachedSeats = bookingService.getCachedAvailableSeats(busId);

            // Merge all seats
            Set<String> allSeats = new LinkedHashSet<>(bookedSeats);

            allSeats.addAll(cachedSeats);

            int totalSeatCount = 0;

            if (!allSeats.isEmpty()) {
                totalSeatCount = allSeats.size();
            }

            // Prepare response
            response.put("busId", busId);

            response.put("journeyDate", journeyDateStr);

            response.put("AllBookedSeats", new ArrayList<>(allSeats));

            response.put("TotalSeat", totalSeats);

            response.put("TotalSeatCount", totalSeatCount);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ReservationException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            response.put("error", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get All Booked Seat", description = "Fetches All Booked buses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of booked seats"),
            @ApiResponse(responseCode = "404", description = "Booked seat not found")
    })
    @GetMapping("/Get-All-booked-seats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookedSeatDTO>> getAllBookedSeats() {
        try {

            List<BookedSeatDTO> bookedSeats = reservationService.getAllBookedSeats();

            return ResponseEntity.ok(bookedSeats);

        } catch (ReservationException e) {

            return ResponseEntity.status(500).body(null);  // You can improve error handling as needed
        }
    }

    @Operation(summary = "Get all reservations for a specific user", description = "Fetches all reservations associated with the provided user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations fetched successfully"),
            @ApiResponse(responseCode = "404", description = "No reservations found for the given user ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getReservation/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getReservationByUserId(@PathVariable String userId) throws ReservationException {
        try {

            List<Reservations> reservations = reservationService.getReservationsByUserId(userId);

            if (reservations == null || reservations.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            return ResponseEntity.ok(reservations);

        } catch (ReservationException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while fetching reservations", "details", ex.getMessage()));
        }
    }


    // Global Exception Handling for ReservationException and LoginException
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<Map<String, Object>> handleReservationException(ReservationException ex) {

        String methodName = "handleReservationException";

        LOGGER.errorLog(CLASSNAME, methodName, "ReservationException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex) {
        String methodName = "handleLoginException";
        LOGGER.errorLog(CLASSNAME, methodName, "LoginException occurred: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}