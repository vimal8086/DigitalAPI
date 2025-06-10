package com.one.digitalapi.service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.one.digitalapi.entity.*;
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
    public Bus updateBus(Long busId, Bus busDetails) {

        Bus existingBus = bRepo.findById(Math.toIntExact(busId))
                .orElseThrow(() -> new BusException("Bus with ID " + busId + " not found"));

        // Update simple fields
        existingBus.setBusName(busDetails.getBusName());
        existingBus.setDriverName(busDetails.getDriverName());
        existingBus.setBusType(busDetails.getBusType());
        existingBus.setRouteFrom(busDetails.getRouteFrom());
        existingBus.setRouteTo(busDetails.getRouteTo());
        existingBus.setArrivalTime(busDetails.getArrivalTime());
        existingBus.setDepartureTime(busDetails.getDepartureTime());
        existingBus.setFarePerSeat(busDetails.getFarePerSeat());
        existingBus.setSeats(busDetails.getSeats());
        existingBus.setAvailableSeats(busDetails.getAvailableSeats());
        existingBus.setContactNumber(busDetails.getContactNumber());
        existingBus.setBusNumber(busDetails.getBusNumber());
        existingBus.setTrackingUrl(busDetails.getTrackingUrl());
        existingBus.setEmail(busDetails.getEmail());
        existingBus.setAddress(busDetails.getAddress());
        existingBus.setActive(busDetails.isActive());

        // Update Route
        Route route = rRepo.findByRouteFromAndRouteTo(busDetails.getRouteFrom(), busDetails.getRouteTo());
        if (route == null) {
            throw new BusException("Invalid route!");
        }
        existingBus.setRoute(route);

        // Handle Amenities
        if (busDetails.getAmenities() != null) {
            for (Amenity amenity : busDetails.getAmenities()) {
                amenity.setBus(existingBus);
            }
            existingBus.getAmenities().clear();
            existingBus.getAmenities().addAll(busDetails.getAmenities());
        }

        // Handle Cancellation Rules
        if (busDetails.getCancellationRules() != null) {
            for (CancellationRule rule : busDetails.getCancellationRules()) {
                rule.setBus(existingBus);
            }
            existingBus.getCancellationRules().clear();
            existingBus.getCancellationRules().addAll(busDetails.getCancellationRules());
        }

        // Handle Pickup Points
        if (busDetails.getPickupPoints() != null) {
            for (PickupPoint pickup : busDetails.getPickupPoints()) {
                pickup.setBus(existingBus);
            }
            existingBus.getPickupPoints().clear();
            existingBus.getPickupPoints().addAll(busDetails.getPickupPoints());
        }

        // Handle Drop Points
        if (busDetails.getDropPoints() != null) {
            for (DropPoint drop : busDetails.getDropPoints()) {
                drop.setBus(existingBus);
            }
            existingBus.getDropPoints().clear();
            existingBus.getDropPoints().addAll(busDetails.getDropPoints());
        }

        return bRepo.save(existingBus);
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