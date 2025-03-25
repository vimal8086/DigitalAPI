package com.one.digitalapi.entity;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class PickupPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Pickup location cannot be null.")
    @NotBlank(message = "Pickup location cannot be blank.")
    @NotEmpty(message = "Pickup location cannot be empty.")
    private String location;

    @NotNull(message = "Pickup address cannot be null.")
    private String address;

    @NotNull(message = "Pickup time cannot be null.")
    private LocalTime pickupTime;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    @JsonBackReference  // 🔥 Prevents infinite recursion
    private Bus bus;

    public PickupPoint() {
    }

    public PickupPoint(String location, String address, LocalTime pickupTime, Bus bus) {
        this.location = location;
        this.address = address;
        this.pickupTime = pickupTime;
        this.bus = bus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}