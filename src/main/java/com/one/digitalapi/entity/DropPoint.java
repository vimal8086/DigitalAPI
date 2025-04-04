package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class DropPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Drop location cannot be null.")
    @NotBlank(message = "Drop location cannot be blank.")
    @NotEmpty(message = "Drop location cannot be empty.")
    private String location;

    @NotNull(message = "Drop address cannot be null.")
    @NotBlank(message = "Drop address cannot be blank.")
    @NotEmpty(message = "Drop address cannot be empty.")
    private String address;

    @NotNull(message = "Drop time cannot be null.")
    @NotBlank(message = "Drop time cannot be blank.")
    @NotEmpty(message = "Drop time cannot be empty.")
    @Schema(description = "Drop time", example = "18:15:00", format = "time")
    @JsonFormat(pattern = "HH:mm:ss") // Ensures correct format when serializing/deserializing JSON
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "Invalid time format. Expected HH:mm:ss")
    private String dropTime;

    @NotNull(message = "Contact Number cannot be null.")
    @NotBlank(message = "Contact Number cannot be blank.")
    @NotEmpty(message = "Contact Number cannot be empty.")
    private String contactNumber;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Contact Number cannot be empty.")
    private String email;


    @ManyToOne
    @JoinColumn(name = "bus_id")
    @JsonBackReference  // 🔥 Prevents infinite recursion
    private Bus bus;

    public DropPoint() {
    }

    public DropPoint(String location, String address, String dropTime, Bus bus, String contactNumber, String email) {
        this.location = location;
        this.address = address;
        this.dropTime = dropTime;
        this.bus = bus;
        this.contactNumber = contactNumber;
        this.email = email;
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

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}