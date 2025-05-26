package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Route;
import com.one.digitalapi.exception.RouteException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.one.digitalapi.exception.GlobalExceptionHandler.getMapResponseEntity;

@RestController
@RequestMapping("/routes")
@Tag(name = "Route Management", description = "APIs for managing routes")
public class RouteController {

    private static final String CLASSNAME = "RouteController";
    private static final DefaultLogger LOGGER = new DefaultLogger(RouteController.class);

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    @Operation(summary = "Add a new route", description = "Creates a new route if it does not exist")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> addRoute(@Valid @RequestBody Route route) {
        String methodName = "addRoute";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to add a new route: " + route);

        // Prevent adding a route where `routeFrom` and `routeTo` are the same
        if (route.getRouteFrom().equalsIgnoreCase(route.getRouteTo())) {
            return ResponseEntity.badRequest().body(Map.of("message", "RouteFrom and RouteTo cannot be the same."));
        }

        // Prevent adding duplicate routes (Check in the database)
        if (routeService.routeExists(route.getRouteFrom(), route.getRouteTo())) {
            return ResponseEntity.badRequest().body(Map.of("message", "This route already exists."));
        }

        Route createdRoute = routeService.addRoute(route);
        LOGGER.infoLog(CLASSNAME, methodName, "Route added successfully: " + createdRoute);

        return ResponseEntity.ok(Map.of(
                "message", "Route added successfully",
                "route", createdRoute
        ));
    }

    @PutMapping
    @Operation(summary = "Update a route using routeId as request param", description = "Update a route using routeId in request parameter")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Route updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateRouteByRequestParam(
            @RequestParam("routeId") int routeId,
            @Valid @RequestBody Route route) {

        String methodName = "updateRouteByRequestParam";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to update route ID: " + routeId + ", Data: " + route);

        // Step 1: Check if the route exists
        Route existingRoute = routeService.viewRoute(routeId);
        if (existingRoute == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Route not found for ID: " + routeId));
        }

        // Prevent same 'routeFrom' and 'routeTo'
        if (route.getRouteFrom().equalsIgnoreCase(route.getRouteTo())) {
            return ResponseEntity.badRequest().body(Map.of("error", "RouteFrom and RouteTo cannot be the same."));
        }

        // Prevent duplicate routes
        if (routeService.routeExists(route.getRouteFrom(), route.getRouteTo())) {
            return ResponseEntity.badRequest().body(Map.of("error", "This route already exists in the database."));
        }

        // Set the routeId to ensure proper update
        route.setRouteID(routeId);
        Route updatedRoute = routeService.updateRoute(route);
        LOGGER.infoLog(CLASSNAME, methodName, "Route updated successfully: " + updatedRoute);

        return ResponseEntity.ok(Map.of(
                "message", "Route updated successfully",
                "route", updatedRoute
        ));
    }



    @DeleteMapping("/{routeId}")
    @Operation(summary = "Delete a route", description = "Delete route with route id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Route deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteRoute(@PathVariable int routeId) {
        String methodName = "deleteRoute";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to delete route with ID: " + routeId);

        routeService.deleteRoute(routeId);

        LOGGER.infoLog(CLASSNAME, methodName, "Route with ID " + routeId + " deleted successfully");

        // JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Route deleted successfully");

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{routeId}")
    @Operation(summary = "Get a route with route id", description = "Get a route with route id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Route found"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Route> viewRoute(@PathVariable int routeId) {
        String methodName = "viewRoute";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to view route with ID: " + routeId);
        Route route = routeService.viewRoute(routeId);
        LOGGER.infoLog(CLASSNAME, methodName, "Route retrieved successfully: " + route);
        return ResponseEntity.ok(route);
    }

    @GetMapping
    @Operation(summary = "Get all routes", description = "Get all routes")
    @ApiResponse(responseCode = "200", description = "List of routes retrieved successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Route>> viewAllRoutes() {
        String methodName = "viewAllRoutes";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to view all routes");
        List<Route> routes = routeService.viewAllRoutes();
        LOGGER.infoLog(CLASSNAME, methodName, "Retrieved " + routes.size() + " routes");
        return ResponseEntity.ok(routes);
    }

    // Global Exception Handling for RouteException and LoginException
    @ExceptionHandler(RouteException.class)
    public ResponseEntity<Map<String, Object>> handleRouteException(RouteException ex) {
        String methodName = "handleRouteException";
        LOGGER.errorLog(CLASSNAME, methodName, "RouteException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }
}