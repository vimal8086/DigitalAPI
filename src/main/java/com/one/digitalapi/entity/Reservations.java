package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reservationId;

    private String reservationStatus;

    @NotNull(message = "This Field can not be null..")
    private String reservationType;

    @NotNull(message = "reservation date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "reservation date must be today or in the future")
    private LocalDateTime reservationDate;

    @NotNull(message = "Journey date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent(message = "Journey date must be today or in the future")
    private LocalDateTime journeyDate;


    @NotNull(message = "Reservation source can not be null..")
    @NotBlank(message = "Reservation source can not be blank..")
    @NotEmpty(message = "Reservation source can not be empty..")
    private String source;

    @NotNull(message = "Reservation destination can not be null..")
    @NotBlank(message = "Reservation destination can not be blank..")
    @NotEmpty(message = "Reservation destination can not be empty..")
    private String destination;

    @NotNull(message = "Number of seats booked cannot be null")
    @Min(value = 1, message = "At least 1 seat must be booked")
    @Positive(message = "No of seat must be greater than zero.") // Must be greater than 0
    private Integer noOfSeatsBooked;

    @PositiveOrZero(message = "Fare must be zero or positive.") // Allows 0
    @NotNull(message = "fare cannot be null")
    private Integer fare;

    private String cancellationReason;

    private Integer refundAmount;

    // New Fields
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Mobile number cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Gender cannot be null")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;

    @ManyToOne(optional = true)
    @Valid
    @JoinColumn(name = "bus_id", referencedColumnName = "busId")
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Passenger> passengers;

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }


    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDateTime journeyDate) {
        this.journeyDate = journeyDate;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNoOfSeatsBooked() {
        return noOfSeatsBooked;
    }

    public void setNoOfSeatsBooked(Integer noOfSeatsBooked) {
        this.noOfSeatsBooked = noOfSeatsBooked;
    }

    public Integer getFare() {
        return fare;
    }

    public void setFare(Integer fare) {
        this.fare = fare;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructor with new fields
    public Reservations(Integer reservationId, String reservationStatus,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String reservationType,
                        LocalDateTime reservationDate, LocalDateTime journeyDate,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String source,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String destination,
                        Integer noOfSeatsBooked, Integer fare, Bus bus, User user, String cancellationReason, Integer refundAmount,
                        String username, String mobileNumber, String email, String gender) {
        super();
        this.reservationId = reservationId;
        this.reservationStatus = reservationStatus;
        this.reservationType = reservationType;
        this.reservationDate = reservationDate;
        this.journeyDate = journeyDate;
        this.source = source;
        this.destination = destination;
        this.noOfSeatsBooked = noOfSeatsBooked;
        this.fare = fare;
        this.bus = bus;
        this.user = user;
        this.cancellationReason = cancellationReason;
        this.refundAmount = refundAmount;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.gender = gender;
    }

    public Reservations() {
        // Default constructor
    }
}
