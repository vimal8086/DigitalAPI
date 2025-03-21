package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Destination;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.DestinationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<Destination> getAllDestinations() {

        String strMethodName="getAllDestinations";

        LOGGER.infoLog(CLASSNAME, strMethodName, "Received request to fetch all destinations");

        List<Destination> destinations = destinationRepository.findAll();

        LOGGER.infoLog(CLASSNAME, strMethodName, "Returning {} destinations" + destinations.size());

        return destinations;
    }

    @PostMapping
    @Operation(summary = "Add a new destination", description = "Creates a new destination if it does not exist")
    public ResponseEntity<?> createDestination(@RequestBody Destination destination) {

        String strMethodName="getAllDestinations";

        LOGGER.infoLog(CLASSNAME, strMethodName, "Received request to create a new destination: {}" + destination.getDestination());

        if (destination.getDestination() == null || destination.getDestination().isEmpty()) {

            LOGGER.warnLog(CLASSNAME, strMethodName, "Failed to create destination: Destination name cannot be empty");

            return ResponseEntity.badRequest().body("Destination name cannot be empty.");
        }

        // Check if the destination already exists
        Optional<Destination> existingDestination = destinationRepository.findByDestination(destination.getDestination());
        if (existingDestination.isPresent()) {
            LOGGER.warnLog(CLASSNAME, strMethodName, "Failed to create destination: '{}' already exists" + destination.getDestination());
            return ResponseEntity.badRequest().body("Destination Already Available.");
        }

        Destination savedDestination = destinationRepository.save(destination);
        LOGGER.infoLog(CLASSNAME, strMethodName, "Successfully created destination: {}" + savedDestination.getDestination());
        return ResponseEntity.ok(savedDestination);
    }
}