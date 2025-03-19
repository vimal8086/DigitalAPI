package com.one.digitalapi.controller;

import com.one.digitalapi.dto.ReservationDTO;
import com.one.digitalapi.entity.Reservations;
import com.one.digitalapi.exception.BusException;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.exception.ReservationException;
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

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new reservation", description = "Creates a new reservation")
    public ResponseEntity<Reservations> addReservation(@Valid @RequestBody ReservationDTO reservationDTO) throws ReservationException, LoginException {
        return ResponseEntity.ok(reservationService.addReservation(reservationDTO));
    }

    @PutMapping("/update")
    @Operation(summary = "Update a reservation", description = "Update a reservation")
    public ResponseEntity<Reservations> updateReservation(@Valid @RequestBody Reservations reservation) throws ReservationException, LoginException {
        return ResponseEntity.ok(reservationService.updateReservation(reservation));
    }

    @GetMapping("/view/{id}")
    @Operation(summary = "Get a reservation with reservation id", description = "Get a reservation with reservation id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<Reservations> viewReservation(@PathVariable Integer id) throws ReservationException, LoginException {
        return ResponseEntity.ok(reservationService.viewAllReservation(id));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all reservations", description = "Get all reservations")
    @ApiResponse(responseCode = "200", description = "List of reservations retrieved successfully")
    public ResponseEntity<List<Reservations>> getAllReservations() throws ReservationException, LoginException {
        return ResponseEntity.ok(reservationService.getReservationDeatials());
    }

    // ✅ Global Exception Handling for BusException and LoginException
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<String> handleReservationException(ReservationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/cancel/{id}")
    @Operation(summary = "Cancel a reservation", description = "Cancel a reservation and calculate refund")
    public ResponseEntity<Reservations> cancelReservation(
            @PathVariable Integer id,
            @RequestParam String cancellationReason) throws ReservationException, LoginException {

        return ResponseEntity.ok(reservationService.deleteReservation(id, cancellationReason));
    }


}