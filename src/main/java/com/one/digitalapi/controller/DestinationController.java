package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Destination;
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

    @Autowired
    private DestinationRepository destinationRepository;

    // GET API - Fetch all destinations
    @GetMapping
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }


    @PostMapping
    @Operation(summary = "Add a new destination", description = "Creates a new destination if it does not exist")
    public ResponseEntity<?> createDestination(@RequestBody Destination destination) {
        if (destination.getDestination() == null || destination.getDestination().isEmpty()) {
            return ResponseEntity.badRequest().body("Destination name cannot be empty.");
        }

        // Check if the destination already exists
        Optional<Destination> existingDestination = destinationRepository.findByDestination(destination.getDestination());
        if (existingDestination.isPresent()) {
            return ResponseEntity.badRequest().body("Destination Already Available.");
        }

        Destination savedDestination = destinationRepository.save(destination);
        return ResponseEntity.ok(savedDestination);
    }
}