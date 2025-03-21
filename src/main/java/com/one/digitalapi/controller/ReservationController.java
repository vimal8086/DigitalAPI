package com.one.digitalapi.controller;

import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.exception.BusException;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation Management", description = "APIs for managing reservations")
public class ReservationController {

    private static final String CLASSNAME = "ReservationController";
    private static final DefaultLogger LOGGER = new DefaultLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
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

    @PutMapping("/update")
    @Operation(summary = "Update a reservation", description = "Update a reservation")
    public ResponseEntity<Reservations> updateReservation(@Valid @RequestBody Reservations reservation) throws ReservationException, LoginException {
        String methodName = "updateReservation";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to update reservation: " + reservation);
        Reservations updatedReservation = reservationService.updateReservation(reservation);
        LOGGER.infoLog(CLASSNAME, methodName, "Reservation updated successfully: " + updatedReservation);
        return ResponseEntity.ok(updatedReservation);
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
    public ResponseEntity<Reservations> cancelReservation(
            @PathVariable Integer id,
            @RequestParam String cancellationReason) throws ReservationException, LoginException {
        String methodName = "cancelReservation";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to cancel reservation with ID: " + id + " for reason: " + cancellationReason);
        Reservations canceledReservation = reservationService.deleteReservation(id, cancellationReason);
        LOGGER.infoLog(CLASSNAME, methodName, "Reservation canceled successfully: " + canceledReservation);
        return ResponseEntity.ok(canceledReservation);
    }

    // Global Exception Handling for ReservationException and LoginException
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<String> handleReservationException(ReservationException ex) {
        String methodName = "handleReservationException";
        LOGGER.errorLog(CLASSNAME, methodName, "ReservationException occurred: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex) {
        String methodName = "handleLoginException";
        LOGGER.errorLog(CLASSNAME, methodName, "LoginException occurred: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}