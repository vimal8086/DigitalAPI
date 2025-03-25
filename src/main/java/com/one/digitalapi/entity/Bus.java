package com.one.digitalapi.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Integer busId;

    @NotNull(message = "Bus Name can not be null.")
    @NotBlank(message = "Bus Name can not be blank.")
    @NotEmpty(message = "Bus Name can not be empty.")
    private String busName;

    @NotNull(message = "Driver Name can not be null.")
    @NotBlank(message = "Driver Name can not be blank.")
    @NotEmpty(message = "Driver Name can not be empty.")
    private String driverName;

    @NotNull(message = "Bus Type can not be null.")
    @NotBlank(message = "Bus Type can not be blank.")
    @NotEmpty(message = "Bus Type can not be empty.")
    private String busType;

    @NotNull(message = "Route From can not be null.")
    @NotBlank(message = "Route From can not be blank.")
    @NotEmpty(message = "Route From can not be empty.")
    private String routeFrom;

    @NotNull(message = "Route To can not be null.")
    @NotBlank(message = "Route To can not be blank.")
    @NotEmpty(message = "Route To can not be empty.")
    private String routeTo;

    @Schema(description = "Bus arrival time", example = "18:15:00", format = "time")
    private LocalTime arrivalTime;

    @Schema(description = "Bus departure time", example = "18:15:00", format = "time")
    private LocalTime departureTime;

    private Integer farePerSeat;

    @Min(value = 1)
    @Max(value = 60)
    private Integer seats;

    private Integer availableSeats;

    @JsonIgnore
    @ManyToOne
    private Route route;

    // ✅ New Fields Added
    @NotNull(message = "Contact Number cannot be null.")
    @NotBlank(message = "Contact Number cannot be blank.")
    private String contactNumber;

    @NotNull(message = "Bus Number cannot be null.")
    @NotBlank(message = "Bus Number cannot be blank.")
    private String busNumber;

    private String trackingUrl;


    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // This prevents infinite recursion
    private List<PickupPoint> pickupPoints = new ArrayList<>();



    public Bus() {
        // Default constructor
    }

    public Bus(Integer busId, String busName, String driverName, String busType, String routeFrom, String routeTo,
               LocalTime arrivalTime, LocalTime departureTime, Integer farePerSeat, Integer seats, Integer availableSeats, Route route,
               String contactNumber, String busNumber, String trackingUrl, List<PickupPoint> pickupPoints) {
        this.busId = busId;
        this.busName = busName;
        this.driverName = driverName;
        this.busType = busType;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.farePerSeat = farePerSeat;
        this.seats = seats;
        this.availableSeats = availableSeats;
        this.route = route;
        this.contactNumber = contactNumber;
        this.busNumber = busNumber;
        this.trackingUrl = trackingUrl;
        this.pickupPoints = pickupPoints;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getFarePerSeat() {
        return farePerSeat;
    }

    public void setFarePerSeat(Integer farePerSeat) {
        this.farePerSeat = farePerSeat;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public List<PickupPoint> getPickupPoints() {
        return pickupPoints;
    }

    public void setPickupPoints(List<PickupPoint> pickupPoints) {
        this.pickupPoints = pickupPoints;
    }
}