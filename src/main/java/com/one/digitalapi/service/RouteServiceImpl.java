package com.one.digitalapi.service;

import com.one.digitalapi.entity.Route;
import com.one.digitalapi.exception.RouteException;
import com.one.digitalapi.repository.RouteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public Route addRoute(Route route) {
        if (routeRepository.existsByRouteFromIgnoreCaseAndRouteToIgnoreCase(route.getRouteFrom(), route.getRouteTo())) {
            throw new RouteException("This route already exists.");
        }

        // Prevent same `routeFrom` and `routeTo`
        if (route.getRouteFrom().equalsIgnoreCase(route.getRouteTo())) {
            throw new RouteException("RouteFrom and RouteTo cannot be the same.");
        }

        return routeRepository.save(route);    }

    @Override
    public Route updateRoute(Route route) {
        // Prevent same 'routeFrom' and 'routeTo'
        if (route.getRouteFrom().equalsIgnoreCase(route.getRouteTo())) {
            throw new RouteException("RouteFrom and RouteTo cannot be the same.");
        }

        // Prevent updating to an already existing route in the database
        boolean routeExists = routeRepository.existsByRouteFromIgnoreCaseAndRouteToIgnoreCase(route.getRouteFrom(), route.getRouteTo());
        if (routeExists) {
            throw new RouteException("This route already exists in the database.");
        }

        return routeRepository.save(route);
    }

    @Override
    public void deleteRoute(int routeId) {

        Route existingRoute = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException("Route not found for ID: " + routeId));

        routeRepository.deleteById(existingRoute.getRouteID());
    }

    @Override
    public Route viewRoute(int routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException("Route not found for ID: " + routeId));
    }

    @Override
    public List<Route> viewAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public boolean routeExists(String routeFrom, String routeTo) {
        return routeRepository.existsByRouteFromIgnoreCaseAndRouteToIgnoreCase(routeFrom, routeTo);
    }
}