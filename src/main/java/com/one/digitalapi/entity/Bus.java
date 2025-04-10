package com.one.digitalapi.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.one.digitalapi.validation.DefaultValidation;
import com.one.digitalapi.validation.FullValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Integer busId;

    @NotNull(message = "Bus Name can not be null.", groups = DefaultValidation.class)
    @NotBlank(message = "Bus Name can not be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "Bus Name can not be empty.", groups = DefaultValidation.class)
    private String busName;

    @NotNull(message = "Driver Name can not be null.", groups = DefaultValidation.class)
    @NotBlank(message = "Driver Name can not be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "Driver Name can not be empty.", groups = DefaultValidation.class)
    private String driverName;

    @NotNull(message = "Bus Type can not be null.", groups = DefaultValidation.class)
    @NotBlank(message = "Bus Type can not be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "Bus Type can not be empty.", groups = DefaultValidation.class)
    private String busType;

    @NotNull(message = "Route From can not be null.", groups = {DefaultValidation.class, FullValidation.class})
    @NotBlank(message = "Route From can not be blank.", groups = {DefaultValidation.class, FullValidation.class})
    @NotEmpty(message = "Route From can not be empty.", groups = {DefaultValidation.class, FullValidation.class})
    private String routeFrom;

    @NotNull(message = "Route To can not be null.", groups = {DefaultValidation.class, FullValidation.class})
    @NotBlank(message = "Route To can not be blank.", groups = {DefaultValidation.class, FullValidation.class})
    @NotEmpty(message = "Route To can not be empty.", groups = {DefaultValidation.class, FullValidation.class})
    private String routeTo;


    @Schema(description = "Bus arrival time", example = "18:15:00", format = "time")
    @JsonFormat(pattern = "HH:mm:ss") // Ensures correct format when serializing/deserializing JSON
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "Invalid time format. Expected HH:mm:ss")
    @NotNull(message = "arrival time can not be null.", groups = DefaultValidation.class)
    @NotBlank(message = "arrival time can not be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "arrival time can not be empty.", groups = DefaultValidation.class)
    private String arrivalTime; // Use String to enforce validation

    @Schema(description = "Bus departure time", example = "18:15:00", format = "time")
    @JsonFormat(pattern = "HH:mm:ss") // Ensures correct format when serializing/deserializing JSON
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "Invalid time format. Expected HH:mm:ss")
    @NotNull(message = "departure time can not be null.", groups = DefaultValidation.class)
    @NotBlank(message = "departure time can not be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "departure time can not be empty.", groups = DefaultValidation.class)
    private String departureTime;

    @NotNull(message = "Fare per seat cannot be null.", groups = DefaultValidation.class) // Ensures value is provided
    @Positive(message = "Fare per seat must be greater than zero.", groups = DefaultValidation.class) // Must be greater than 0
    @PositiveOrZero(message = "Fare per seat must be zero or positive.", groups = DefaultValidation.class) // Allows 0
    private Integer farePerSeat;

    @Min(value = 1)
    @Max(value = 60)
    private Integer seats;

    private Integer availableSeats;

    @JsonIgnore
    @ManyToOne
    @Valid
    private Route route;

    // New Fields Added
    @NotNull(message = "Contact Number cannot be null.", groups = DefaultValidation.class)
    @NotBlank(message = "Contact Number cannot be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "Contact Number cannot be Empty", groups = DefaultValidation.class)
    private String contactNumber;

    @NotNull(message = "Bus Number cannot be null.", groups = DefaultValidation.class)
    @NotBlank(message = "Bus Number cannot be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "Bus Number cannot be Empty", groups = DefaultValidation.class)
    private String busNumber;

    private String trackingUrl;

    @NotNull(message = "Email cannot be null", groups = DefaultValidation.class)
    @Email(message = "Invalid email format", groups = DefaultValidation.class)
    @NotEmpty(message = "Email cannot be Empty", groups = DefaultValidation.class)
    @Email(message = "Invalid email format", groups = DefaultValidation.class)
    private String email;

    @NotNull(message = "Pickup address cannot be null.", groups = DefaultValidation.class)
    @NotBlank(message = "Pickup address can not be blank.", groups = DefaultValidation.class)
    @NotEmpty(message = "Pickup address can not be empty.", groups = DefaultValidation.class)
    private String address;


    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // This prevents infinite recursion
    @Valid
    private List<PickupPoint> pickupPoints = new ArrayList<>();


    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // This prevents infinite recursion
    @Valid
    private List<DropPoint> dropPoints = new ArrayList<>();


    public Bus() {
        // Default constructor
    }

    public Bus(Integer busId, String busName, String driverName, String busType, String routeFrom, String routeTo,
               String arrivalTime, String departureTime, Integer farePerSeat, Integer seats, Integer availableSeats, Route route,
               String contactNumber, String busNumber, String trackingUrl, List<PickupPoint> pickupPoints, List<DropPoint> dropPoints, String email,String address) {
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
        this.dropPoints = dropPoints;
        this.email = email;
        this.address = address;
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

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
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

    public List<DropPoint> getDropPoints() {
        return dropPoints;
    }

    public void setDropPoints(List<DropPoint> dropPoints) {
        this.dropPoints = dropPoints;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}