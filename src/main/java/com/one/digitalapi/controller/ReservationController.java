package com.one.digitalapi.controller;

import com.one.digitalapi.dto.BookedSeatDTO;
import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.BookingService;
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
import org.springframework.web.bind.annotation.*;

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

    private final PdfService pdfService;



    public ReservationController(ReservationService reservationService, BookingService bookingService, PdfService pdfService) {
        this.reservationService = reservationService;
        this.bookingService = bookingService;
        this.pdfService = pdfService;
    }


    @GetMapping("/generate/{reservationId}")
    @Operation(summary = "Generate PDF for ticket", description = "Generate PDF for ticket")
    public ResponseEntity<byte[]> generateTicket(@PathVariable Integer reservationId) {
        try {
            byte[] pdfBytes = pdfService.generateFormattedTicket(reservationId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_" + reservationId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new reservation", description = "Creates a new reservation")
    public ResponseEntity<Reservations> addReservation(@Valid @RequestBody ReservationDTO reservationDTO) throws ReservationException, LoginException {
        String methodName = "addReservation";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to add reservation: " + reservationDTO);
        Reservations reservation = reservationService.addReservation(reservationDTO);
        LOGGER.infoLog(CLASSNAME, methodName, "Reservation added successfully: " + reservation);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/view/{id}")
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
    @Operation(summary = "Get all reservations", description = "Get all reservations")
    @ApiResponse(responseCode = "200", description = "List of reservations retrieved successfully")
    public ResponseEntity<List<Reservations>> getAllReservations() throws ReservationException, LoginException {
        String methodName = "getAllReservations";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to retrieve all reservations");
        List<Reservations> reservations = reservationService.getReservationDeatials();
        LOGGER.infoLog(CLASSNAME, methodName, "Reservations retrieved successfully: " + reservations);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/cancel/{id}")
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

            // Prepare response
            response.put("busId", busId);
            response.put("journeyDate", journeyDateStr);
            response.put("AllBookedSeats", new ArrayList<>(allSeats));

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
    public ResponseEntity<List<BookedSeatDTO>> getAllBookedSeats() {
        try {
            List<BookedSeatDTO> bookedSeats = reservationService.getAllBookedSeats();
            return ResponseEntity.ok(bookedSeats);
        } catch (ReservationException e) {
            return ResponseEntity.status(500).body(null);  // You can improve error handling as needed
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