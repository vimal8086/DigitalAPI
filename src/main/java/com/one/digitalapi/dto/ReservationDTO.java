package com.one.digitalapi.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ReservationDTO {

    @NotNull(message = "Start point cannot be null!")
    @NotBlank(message = "Start point cannot be blank!")
    private String source;

    @NotNull(message = "Destination point cannot be null!")
    @NotBlank(message = "Destination point cannot be blank!")
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

    private List<PassengerDTO> passengers;


    public ReservationDTO() {
        super();
    }

    public ReservationDTO(
            String source, String destination, Integer noOfSeatsToBook, LocalDateTime journeyDate,
            BusDTO busDTO, String userId, String username, String mobileNumber, String email, String gender, List<PassengerDTO> passengers) {
        super();
        this.source = source;
        this.destination = destination;
        this.noOfSeatsToBook = noOfSeatsToBook;
        this.journeyDate = journeyDate;
        this.busDTO = busDTO;
        this.userId = userId;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.gender = gender;
        this.passengers = passengers;
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
}