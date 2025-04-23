package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Destination;
import com.one.digitalapi.exception.DestinationException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.DestinationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/destinations")
@Tag(name = "Destination Management", description = "APIs for managing destinations")
public class DestinationController {

    private static final String CLASSNAME="DestinationController";

    private static final DefaultLogger LOGGER = new DefaultLogger(DestinationController.class);

    @Autowired
    private DestinationRepository destinationRepository;

    // GET API - Fetch all destinations
    @GetMapping
    @Operation(summary = "Fetch all destinations", description = "Retrieves the list of all available destinations")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Destination> getAllDestinations() {

        String strMethodName="getAllDestinations";

        LOGGER.infoLog(CLASSNAME, strMethodName, "Received request to fetch all destinations");

        List<Destination> destinations = destinationRepository.findAll();

        LOGGER.infoLog(CLASSNAME, strMethodName, "Returning {} destinations" + destinations.size());

        return destinations;
    }

    @PostMapping
    @Operation(summary = "Add a new destination", description = "Creates a new destination if it does not exist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDestination(@RequestBody Destination destination) {

        String strMethodName="getAllDestinations";

        LOGGER.infoLog(CLASSNAME, strMethodName, "Received request to create a new destination: {}" + destination.getDestination());

        Map<String, String> response = new HashMap<>();

        if (destination.getDestination() == null || destination.getDestination().isEmpty()) {

            LOGGER.warnLog(CLASSNAME, strMethodName, "Failed to create destination: Destination name cannot be empty");

            response.put("error", "Destination name cannot be empty");

            return ResponseEntity.badRequest().body(response);
        }

        // Check if the destination already exists
        Optional<Destination> existingDestination = destinationRepository.findByDestination(destination.getDestination());
        if (existingDestination.isPresent()) {
            LOGGER.warnLog(CLASSNAME, strMethodName, "Failed to create destination: '{}' already exists" + destination.getDestination());

            response.put("error", "Destination Already Available");

            return ResponseEntity.badRequest().body(response);
        }

        Destination savedDestination = destinationRepository.save(destination);
        LOGGER.infoLog(CLASSNAME, strMethodName, "Successfully created destination: {}" + savedDestination.getDestination());
        return ResponseEntity.ok(savedDestination);
    }

    // Global Exception Handling for DestinationException and LoginException
    @ExceptionHandler(DestinationException.class)
    public ResponseEntity<Map<String, Object>> handleDestinationException(DestinationException ex) {
        String methodName = "handleDestinationException";
        LOGGER.errorLog(CLASSNAME, methodName, "DestinationException occurred: " + ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "Destination Not Found");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}