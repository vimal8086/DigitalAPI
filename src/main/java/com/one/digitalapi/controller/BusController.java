package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.entity.Review;
import com.one.digitalapi.exception.BusException;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.BookingService;
import com.one.digitalapi.service.BusService;
import com.one.digitalapi.service.ReservationService;
import com.one.digitalapi.service.ReviewService;
import com.one.digitalapi.validation.DefaultValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.one.digitalapi.exception.GlobalExceptionHandler.getMapResponseEntity;

@RestController
@RequestMapping("/buses")
@Tag(name = "Bus Management", description = "APIs for managing buses")
public class BusController {

    private static final String CLASSNAME = "BusController";
    private static final DefaultLogger LOGGER = new DefaultLogger(BusController.class);

    @Autowired
    private BusService busService;

    private final ReservationService reservationService;

    private final BookingService bookingService;

    private final ReviewService reviewService;

    public BusController(ReservationService reservationService, BookingService bookingService, ReviewService reviewService) {
        this.reservationService = reservationService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
    }

    @PostMapping
    @Operation(summary = "Add a new bus", description = "Creates a new bus if it does not exist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bus> createBus(@Validated(DefaultValidation.class) @RequestBody Bus bus) {
        String methodName = "createBus";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to create bus: " + bus);
        Bus createdBus = busService.addBus(bus);
        LOGGER.infoLog(CLASSNAME, methodName, "Bus created successfully: " + createdBus);
        return new ResponseEntity<>(createdBus, HttpStatus.OK);
    }

    @GetMapping("/find/{busId}")
    @Operation(summary = "Get a bus with bus id", description = "Get a bus with bus id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bus found"),
            @ApiResponse(responseCode = "404", description = "Bus not found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Bus> getBusById(@PathVariable int busId) {
        String methodName = "getBusById";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to get bus with ID: " + busId);
        Bus bus = busService.viewBus(busId);
        LOGGER.infoLog(CLASSNAME, methodName, "Bus retrieved successfully: " + bus);
        return new ResponseEntity<>(bus, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update a bus", description = "Update a bus")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bus updated successfully"),
            @ApiResponse(responseCode = "404", description = "Bus not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bus> updateBusById(@Validated(DefaultValidation.class) @RequestBody Bus bus) {
        String methodName = "updateBusById";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to update bus: " + bus);
        Bus updatedBus = busService.updateBus(bus);
        LOGGER.infoLog(CLASSNAME, methodName, "Bus updated successfully: " + updatedBus);
        return new ResponseEntity<>(updatedBus, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{busId}")
    @Operation(summary = "Delete a bus", description = "Delete bus with bus id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bus deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Bus not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteBusById(@PathVariable int busId) {
        String methodName = "deleteBusById";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to delete bus with ID: " + busId);
        Bus deletedBus = busService.deleteBus(busId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bus deleted successfully");
        response.put("bus", deletedBus);
        LOGGER.infoLog(CLASSNAME, methodName, "Bus deleted successfully: " + deletedBus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{busType}")
    @Operation(summary = "Get buses by type", description = "Get a list of buses filtered by type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buses found"),
            @ApiResponse(responseCode = "404", description = "No buses found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Bus>> getBusesByType(@PathVariable("busType") String busType) {
        String methodName = "getBusesByType";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to get buses of type: " + busType);
        List<Bus> buses = busService.viewBusByType(busType);
        LOGGER.infoLog(CLASSNAME, methodName, "Buses retrieved successfully: " + buses);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all buses", description = "Get all buses")
    @ApiResponse(responseCode = "200", description = "List of buses retrieved successfully")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Bus>> getAllBuses() {
        String methodName = "getAllBuses";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to get all buses");
        List<Bus> buses = busService.viewAllBuses();
        LOGGER.infoLog(CLASSNAME, methodName, "All buses retrieved successfully: " + buses);
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search buses by route, time and journeyDate", description = "Filter buses by 'from', 'to', 'departureTime' and 'journeyDate', It Return All Buses Details With Available and booked seat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buses found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "No buses found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchBuses(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String departureTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String journeyDate) {

        String methodName = "searchBuses";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to search buses from " + from + " to " + to + " at " + departureTime);

        LocalDate journeyDateParsed;
        try {
            journeyDateParsed = LocalDate.parse(journeyDate);
        } catch (DateTimeParseException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid date format. Please use yyyy-MM-dd (e.g., 2024-04-05).");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        List<Bus> buses = busService.searchBuses(from, to, departureTime);

        if (buses == null || buses.isEmpty()) {
            LOGGER.infoLog(CLASSNAME, methodName, "No buses found. Returning empty array.");
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        LocalDateTime journeyStart = journeyDateParsed.atStartOfDay();
        LocalDateTime journeyEnd = journeyDateParsed.atTime(LocalTime.MAX);

        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Bus bus : buses) {

            int totalSeats = bus.getSeats();

            Integer busId = bus.getBusId();

            // Booked seats from reservation service
            List<String> bookedSeats = reservationService.getBookedSeatsForBus(busId, journeyStart, journeyEnd);

            // Cached available seats (optional logic - use if applicable)
            List<String> cachedSeats = bookingService.getCachedAvailableSeats(busId);

            Set<String> allBookedSeats = new LinkedHashSet<>();
            if (bookedSeats != null) allBookedSeats.addAll(bookedSeats);
            if (cachedSeats != null) allBookedSeats.addAll(cachedSeats);

            // For Available Seat
            int availableSeats = Math.max(0, totalSeats - allBookedSeats.size());

            // Average Rating
            Double averageRating = reviewService.getAverageRatingForBus(busId.longValue());

            // Total Reviews
            List<Review> totalReviews = reviewService.getReviewsByBusId(busId.longValue());
            int totalReviewCount = (totalReviews != null) ? totalReviews.size() : 0;

            Map<String, Object> busInfo = new HashMap<>();
            busInfo.put("busId", busId);
            busInfo.put("busName", bus.getBusName());
            busInfo.put("busNumber", bus.getBusNumber());
            busInfo.put("farePerSeat", bus.getFarePerSeat());
            busInfo.put("from", bus.getRouteFrom());
            busInfo.put("to", bus.getRouteTo());
            busInfo.put("arrivalTime", bus.getArrivalTime());
            busInfo.put("departureTime", bus.getDepartureTime());
            busInfo.put("journeyDate", journeyDateParsed);
            busInfo.put("totalSeats", totalSeats);
            busInfo.put("bookedSeats", new ArrayList<>(allBookedSeats));
            busInfo.put("availableSeat", availableSeats);
            busInfo.put("busType", bus.getBusType());
            busInfo.put("pickupPoints", bus.getPickupPoints());
            busInfo.put("dropPoints", bus.getDropPoints());
            busInfo.put("averageRating", averageRating);
            busInfo.put("totalRating", totalReviewCount);
            busInfo.put("amenities", bus.getAmenities());
            busInfo.put("cancellationRules", bus.getCancellationRules());
            busInfo.put("reviews", totalReviews);


            responseList.add(busInfo);
        }

        LOGGER.infoLog(CLASSNAME, methodName, "Returning " + responseList.size() + " buses with booking info.");
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // Global Exception Handling for BusException and LoginException
    @ExceptionHandler(BusException.class)
    public ResponseEntity<Map<String, Object>> handleBusException(BusException ex) {
        String methodName = "handleBusException";
        LOGGER.errorLog(CLASSNAME, methodName, "BusException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Map<String, Object>> handleLoginException(LoginException ex) {
        String methodName = "handleLoginException";
        LOGGER.errorLog(CLASSNAME, methodName, "LoginException occurred: " + ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}