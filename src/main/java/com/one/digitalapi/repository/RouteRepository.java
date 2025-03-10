package com.one.digitalapi.repository;

import com.one.digitalapi.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    public Route findByRouteFromAndRouteTo(String from, String to);

}
