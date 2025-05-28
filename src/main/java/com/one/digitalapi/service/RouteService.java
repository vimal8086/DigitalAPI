package com.one.digitalapi.service;

import com.one.digitalapi.entity.Route;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RouteService {
    Route addRoute(Route route);
    Route updateRoute(Route route);
    void deleteRoute(int routeId);
    Route viewRoute(int routeId);
    List<Route> viewAllRoutes();
    boolean routeExists(String routeFrom, String routeTo);
}
