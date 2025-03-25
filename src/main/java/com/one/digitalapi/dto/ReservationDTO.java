package com.one.digitalapi.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate journeyDate;

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

    public ReservationDTO() {
        super();
    }

    public ReservationDTO(
            String source, String destination, Integer noOfSeatsToBook, LocalDate journeyDate,
            BusDTO busDTO, String userId, String username, String mobileNumber, String email, String gender) {
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

    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }

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
}