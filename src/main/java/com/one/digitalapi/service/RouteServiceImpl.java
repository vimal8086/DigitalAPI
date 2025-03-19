package com.one.digitalapi.service;

import com.one.digitalapi.entity.Route;
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
        return routeRepository.save(route);
    }

    @Override
    public Route updateRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public void deleteRoute(int routeId) {
        routeRepository.deleteById(routeId);
    }

    @Override
    public Route viewRoute(int routeId) {
        return routeRepository.findById(routeId).orElse(null);
    }

    @Override
    public List<Route> viewAllRoutes() {
        return routeRepository.findAll();
    }
}