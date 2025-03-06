package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Route;
import com.one.digitalapi.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@Tag(name = "Route Management", description = "APIs for managing routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    @Operation(summary = "Add a new route", description = "Creates a new route if it does not exist")
    public ResponseEntity<Route> addRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeService.addRoute(route));
    }

    @PutMapping
    @Operation(summary = "Update a route", description = "Update a route")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Route updated successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<Route> updateRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeService.updateRoute(route));
    }

    @DeleteMapping("/{routeId}")
    @Operation(summary = "Delete a route", description = "Delete route with route id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Route deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<String> deleteRoute(@PathVariable int routeId) {
        routeService.deleteRoute(routeId);
        return ResponseEntity.ok("Route deleted successfully");
    }

    @GetMapping("/{routeId}")
    @Operation(summary = "Get a route with route id", description = "Get a route with route id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Route found"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<Route> viewRoute(@PathVariable int routeId) {
        return ResponseEntity.ok(routeService.viewRoute(routeId));
    }

    @GetMapping
    @Operation(summary = "Get all route", description = "Get All Routes")
    @ApiResponse(responseCode = "200", description = "List of routes retrieved successfully")
    public ResponseEntity<List<Route>> viewAllRoutes() {
        return ResponseEntity.ok(routeService.viewAllRoutes());
    }
}