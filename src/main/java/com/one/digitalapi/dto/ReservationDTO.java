package com.one.digitalapi.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ReservationDTO {

    @NotNull(message = "source cannot be null!")
    @NotBlank(message = "Start point cannot be blank!")
    private String source;

    @NotNull(message = "Destination cannot be null!")
    @NotBlank(message = "Destination cannot be blank!")
    private String destination;

    @NotNull
    private Integer noOfSeatsToBook;

    @NotNull(message = "Journey date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent(message = "Journey date must be today or in the future")
    private LocalDateTime journeyDate;

    private BusDTO busDTO;
    private String userId;

    // New Fields
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    @NotBlank(message = "Mobile number cannot be blank!")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number!")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Gender cannot be blank!")
    private String gender;

    @NotNull(message = "Pickup address cannot be null.")
    @NotBlank(message = "Pickup address cannot be blank.")
    @NotEmpty(message = "Pickup address cannot be empty.")
    private String pickupAddress;

    @NotNull(message = "Pickup time cannot be null.")
    @NotBlank(message = "Pickup time cannot be blank.")
    @NotEmpty(message = "Pickup time cannot be empty.")
    @Schema(description = "Pickup time", example = "18:15:00", format = "time")
    @JsonFormat(pattern = "HH:mm:ss") // Ensures correct format when serializing/deserializing JSON
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "Invalid time format. Expected HH:mm:ss")
    private String pickupTime;

    @NotNull(message = "drop address cannot be null.")
    @NotBlank(message = "drop address cannot be blank.")
    @NotEmpty(message = "drop address cannot be empty.")
    private String dropAddress;

    @NotNull(message = "drop time cannot be null.")
    @NotBlank(message = "drop time cannot be blank.")
    @NotEmpty(message = "drop time cannot be empty.")
    @Schema(description = "drop time", example = "18:15:00", format = "time")
    @JsonFormat(pattern = "HH:mm:ss") // Ensures correct format when serializing/deserializing JSON
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$", message = "Invalid time format. Expected HH:mm:ss")
    private String dropTime;

    private String orderId;

    private Double gstAmount;

    private Double totalAmount;

    private Double discountAmount;


    private List<PassengerDTO> passengers;


    public ReservationDTO() {
        super();
    }

    public ReservationDTO(String userId, String source, String destination, Integer noOfSeatsToBook, LocalDateTime journeyDate, BusDTO busDTO, String username, String mobileNumber, String email, String gender, String pickupAddress, String pickupTime, String dropAddress, String dropTime, String orderId, Double gstAmount, Double totalAmount, List<PassengerDTO> passengers, Double discountAmount) {
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.noOfSeatsToBook = noOfSeatsToBook;
        this.journeyDate = journeyDate;
        this.busDTO = busDTO;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.gender = gender;
        this.pickupAddress = pickupAddress;
        this.pickupTime = pickupTime;
        this.dropAddress = dropAddress;
        this.dropTime = dropTime;
        this.orderId = orderId;
        this.gstAmount = gstAmount;
        this.totalAmount = totalAmount;
        this.passengers = passengers;
        this.discountAmount = discountAmount;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Integer getNoOfSeatsToBook() { return noOfSeatsToBook; }
    public void setNoOfSeatsToBook(Integer noOfSeatsToBook) { this.noOfSeatsToBook = noOfSeatsToBook; }

    public LocalDateTime getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDateTime journeyDate) { this.journeyDate = journeyDate; }

    public BusDTO getBusDTO() { return busDTO; }
    public void setBusDTO(BusDTO busDTO) { this.busDTO = busDTO; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(Double gstAmount) {
        this.gstAmount = gstAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }
}