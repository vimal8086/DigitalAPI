package com.one.digitalapi.controller;

import com.one.digitalapi.entity.BookingDetails;
import com.one.digitalapi.entity.Passenger;
import com.one.digitalapi.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/booking")
@Tag(name = "Booking Management", description = "APIs for managing booking")
public class BookingController {

    private final BookingService bookingService;  // Inject BookingService

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings/intermediate-seat")
    @Operation(summary = "Add a new intermediate seat", description = "Creates a new intermediate seat if it does not exist")
    public ResponseEntity<Map<String, Object>> bookIntermediateSeat(@RequestBody BookingDetails bookingDetails) {
        bookingService.cacheBooking(bookingDetails);  // ✅ Use BookingService to store cache

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Seats booked successfully and will be held for 15 minutes!");
        response.put("busId", bookingDetails.getBusId());
        response.put("date", bookingDetails.getDate());
        response.put("seats", bookingDetails.getPassengerList().stream().map(Passenger::getSeatName).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-booking/{busId}/{date}")
    @Operation(summary = "Get booking by busId and date", description = "Fetches booking details for a specific bus and date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "No booking found")
    })
    public ResponseEntity<Map<String, Object>> getBooking(@PathVariable String busId, @PathVariable String date) {
        BookingDetails bookingDetails = bookingService.getCachedBooking(busId, date);  // ✅ Fetch from service

        if (bookingDetails == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "No booking found for busId " + busId + " on date " + date);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // ✅ Booking found, return details
        Map<String, Object> response = new HashMap<>();
        response.put("busId", busId);
        response.put("date", date);
        response.put("passengers", bookingDetails.getPassengerList().stream()
                .map(passenger -> Map.of(
                        "name", passenger.getName(),
                        "email", passenger.getEmail(),
                        "seatName", passenger.getSeatName()
                ))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

}