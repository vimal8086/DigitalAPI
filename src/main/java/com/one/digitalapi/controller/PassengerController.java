package com.one.digitalapi.controller;

import com.one.digitalapi.entity.PassengerRef;
import com.one.digitalapi.exception.PassengerException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.one.digitalapi.exception.GlobalExceptionHandler.getMapResponseEntity;

@RestController
@RequestMapping("/passengers")
@Tag(name = "Passenger Management", description = "APIs for managing passenger")
public class PassengerController {

    private static final String CLASSNAME = "PassengerController";
    private static final DefaultLogger LOGGER = new DefaultLogger(PassengerController.class);

    @Autowired
    private PassengerService passengerService;

    @PostMapping(value = "/user/save/{userId}", consumes = {"application/json", "application/json;charset=UTF-8"})
    @Operation(summary = "Save Passenger for reservation", description = "Save passenger for reservation")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PassengerRef> savePassenger(@Valid @PathVariable String userId, @RequestBody PassengerRef passenger) {
        String methodName = "savePassenger";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to save passenger for userId: " + userId + ", data: " + passenger);
        PassengerRef saved = passengerService.savePassengerForUser(userId, passenger);
        LOGGER.infoLog(CLASSNAME, methodName, "Passenger saved successfully: " + saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/all/{userId}")
    @Operation(summary = "Get All Passengers by User ID", description = "Fetch all passengers for the given user ID")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PassengerRef>> getPassengersByUserId(@PathVariable String userId) {
        String methodName = "getPassengersByUserId";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to fetch all passengers for userId: " + userId);
        List<PassengerRef> passengers = passengerService.getPassengersByUserId(userId);
        LOGGER.infoLog(CLASSNAME, methodName, "Number of passengers found: " + passengers.size());
        return ResponseEntity.ok(passengers);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Passenger", description = "Update existing passenger details")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PassengerRef> updatePassenger(@PathVariable Integer id, @RequestBody PassengerRef passenger) {
        String methodName = "updatePassenger";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to update passenger with ID: " + id + ", new data: " + passenger);
        PassengerRef updated = passengerService.updatePassenger(id, passenger);
        LOGGER.infoLog(CLASSNAME, methodName, "Passenger updated successfully: " + updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Passenger", description = "Delete passenger by ID")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deletePassenger(@PathVariable Integer id) {
        String methodName = "deletePassenger";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to delete passenger with ID: " + id);
        passengerService.deletePassenger(id);
        LOGGER.infoLog(CLASSNAME, methodName, "Passenger deleted successfully for ID: " + id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Passenger deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a passenger with passenger id", description = "Get a passenger with passenger id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Passenger found"),
            @ApiResponse(responseCode = "404", description = "Passenger not found")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> viewPassenger(@PathVariable int id) {
        String methodName = "viewPassenger";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to view passenger with ID: " + id);

        Optional<PassengerRef> passengerRef = passengerService.viewPassenger(id);

        if (passengerRef.isEmpty()) {
            LOGGER.warnLog(CLASSNAME, methodName, "Passenger not found with ID: " + id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Passenger not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        LOGGER.infoLog(CLASSNAME, methodName, "Passenger retrieved successfully: " + passengerRef);
        return ResponseEntity.ok(passengerRef);
    }


    @ExceptionHandler(PassengerException.class)
    public ResponseEntity<Map<String, Object>> handlePassengerException(PassengerException ex) {
        String methodName = "handlePassengerException";
        LOGGER.errorLog(CLASSNAME, methodName, "PassengerException occurred: " + ex.getMessage());
        return getMapResponseEntity(ex.getMessage(), ex);
    }
}