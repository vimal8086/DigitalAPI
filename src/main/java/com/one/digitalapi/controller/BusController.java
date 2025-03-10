package com.one.digitalapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.exception.BusException;
import com.one.digitalapi.exception.LoginException;
import com.one.digitalapi.service.BusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/buses")
@Tag(name = "Bus Management", description = "APIs for managing buses")
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping
    @Operation(summary = "Add a new bus", description = "Creates a new bus if it does not exist")
    public ResponseEntity<Bus> createBus(@Valid @RequestBody Bus bus) {

        return new ResponseEntity<>(busService.addBus(bus), HttpStatus.OK);
    }

    @GetMapping("/find/{busId}")
    @Operation(summary = "Get a bus with bus id", description = "Get a bus with bus id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bus found"),
            @ApiResponse(responseCode = "404", description = "Bus not found")
    })
    public ResponseEntity<Bus> getBusById(@PathVariable int busId) {

        return new ResponseEntity<>(busService.viewBus(busId), HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update a bus", description = "Update a bus")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bus updated successfully"),
            @ApiResponse(responseCode = "404", description = "Bus not found")
    })
    public ResponseEntity<Bus> updateBusById(@Valid @RequestBody Bus bus) {

        return new ResponseEntity<>(busService.updateBus(bus), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{busId}")
    @Operation(summary = "Delete a bus", description = "Delete bus with bus id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bus deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Bus not found")
    })    public ResponseEntity<Map<String, Object>> deleteBusById(@PathVariable int busId) {
        Bus deletedBus = busService.deleteBus(busId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bus deleted successfully");
        response.put("bus", deletedBus);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/type/{busType}")
    @Operation(summary = "Get buses by type", description = "Get a list of buses filtered by type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buses found"),
            @ApiResponse(responseCode = "404", description = "No buses found")
    })
    public ResponseEntity<List<Bus>> getBusesByType(@PathVariable("busType") String busType) {

        return new ResponseEntity<>(busService.viewBusByType(busType), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all buses", description = "Get all buses")
    @ApiResponse(responseCode = "200", description = "List of buses retrieved successfully")
    public ResponseEntity<List<Bus>> getAllBuses() {

        return new ResponseEntity<>(busService.viewAllBuses(), HttpStatus.OK);
    }

    // ✅ Global Exception Handling for BusException and LoginException
    @ExceptionHandler(BusException.class)
    public ResponseEntity<String> handleBusException(BusException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}