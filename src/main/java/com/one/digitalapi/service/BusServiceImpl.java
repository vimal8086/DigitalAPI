package com.one.digitalapi.service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.one.digitalapi.entity.Amenity;
import com.one.digitalapi.entity.Bus;
import com.one.digitalapi.entity.CancellationRule;
import com.one.digitalapi.entity.Route;
import com.one.digitalapi.exception.BusException;
import com.one.digitalapi.repository.BusRepository;
import com.one.digitalapi.repository.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private RouteRepository rRepo;

    @Autowired
    private BusRepository bRepo;

    @Override
    @Transactional
    public Bus addBus(Bus bus) {
        Route route = rRepo.findByRouteFromAndRouteTo(bus.getRouteFrom(), bus.getRouteTo());

        if (route == null) {
            throw new BusException("Route not found from " + bus.getRouteFrom() + " to " + bus.getRouteTo());
        }

        bus.setRoute(route);

        // First time it takes true
        bus.setActive(true);

        // Handle amenities
        List<Amenity> finalAmenities = new ArrayList<>();
        if (bus.getAmenities() != null) {
            for (Amenity amenity : bus.getAmenities()) {
                amenity.setBus(bus);
                finalAmenities.add(amenity);
            }
        }
        bus.setAmenities(finalAmenities); // avoid assigning pre-attached list

        // Handle cancellation rules
        List<CancellationRule> finalRules = new ArrayList<>();
        if (bus.getCancellationRules() != null) {
            for (CancellationRule rule : bus.getCancellationRules()) {
                rule.setBus(bus);
                finalRules.add(rule);
            }
        }
        bus.setCancellationRules(finalRules);

        return bRepo.save(bus);
    }


    @Override
    public Bus updateBus(Bus bus) {
        Bus existingBus = bRepo.findById(bus.getBusId())
                .orElseThrow(() -> new BusException("Bus with ID " + bus.getBusId() + " not found"));

        Route route = rRepo.findByRouteFromAndRouteTo(bus.getRouteFrom(), bus.getRouteTo());
        if (route == null) {
            throw new BusException("Invalid route!");
        }

        bus.setRoute(route);

        // User Can Deactivate Bus
        bus.setActive(bus.isActive());


        // Handle amenities
        List<Amenity> finalAmenities = new ArrayList<>();
        if (bus.getAmenities() != null) {
            for (Amenity amenity : bus.getAmenities()) {
                amenity.setBus(bus);
                finalAmenities.add(amenity);
            }
        }
        bus.setAmenities(finalAmenities); // avoid assigning pre-attached list

        // Handle cancellation rules
        List<CancellationRule> finalRules = new ArrayList<>();
        if (bus.getCancellationRules() != null) {
            for (CancellationRule rule : bus.getCancellationRules()) {
                rule.setBus(bus);
                finalRules.add(rule);
            }
        }
        bus.setCancellationRules(finalRules);
        return bRepo.save(bus);
    }

    @Override
    public Bus deleteBus(int busId) {
        Bus existingBus = bRepo.findById(busId)
                .orElseThrow(() -> new BusException("Bus not found for ID: " + busId));

        bRepo.deleteById(busId);

        return existingBus;
    }

    @Override
    public Bus viewBus(int busId) {
        return bRepo.findById(busId)
                .orElseThrow(() -> new BusException("Bus not found for ID: " + busId));
    }

    @Override
    public List<Bus> viewBusByType(String busType) {
        List<Bus> buses = bRepo.findByBusType(busType);
        if (buses.isEmpty()) {
            throw new BusException("No buses found of type: " + busType);
        }
        return buses;
    }

    @Override
    public List<Bus> viewAllBuses() {
        List<Bus> buses = bRepo.findAll();
        if (buses.isEmpty()) {
            throw new BusException("No buses available in the system");
        }
        return buses;
    }

    @Override
    public List<Bus> searchBuses(String from, String to, String departureTime) {

        List<Bus> buses;

        try {
            if (departureTime != null && !departureTime.isEmpty()) {
                // Validate and parse the departure time
                LocalTime departureTimeParsed = LocalTime.parse(departureTime);

                // Call the repository method with LocalTime
                buses = bRepo.findByRouteFromAndRouteToAndDepartureTimeAfterAndIsActiveTrue(from, to, String.valueOf(departureTimeParsed));
            } else {
                buses = bRepo.findByRouteFromAndRouteToAndIsActiveTrue(from, to);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid departure time format. Expected format is HH:mm:ss");
        }

        return buses;
    }
}