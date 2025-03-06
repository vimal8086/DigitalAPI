package com.one.digitalapi.service;

import com.one.digitalapi.entity.Route;
import java.util.List;

public interface RouteService {
    Route addRoute(Route route);
    Route updateRoute(Route route);
    void deleteRoute(int routeId);
    Route viewRoute(int routeId);
    List<Route> viewAllRoutes();
}
