package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Reservations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reservationId;

    private String reservationStatus;

    @NotNull(message = "This Field can not be null..")
    private String reservationType;

    private LocalDate reservationDate;
    private LocalDate journeyDate;
    private LocalTime reservationTime;

    @NotNull(message = "Reservation source can not be null..")
    @NotBlank(message = "Reservation source can not be blank..")
    @NotEmpty(message = "Reservation source can not be empty..")
    private String source;

    @NotNull(message = "Reservation destination can not be null..")
    @NotBlank(message = "Reservation destination can not be blank..")
    @NotEmpty(message = "Reservation destination can not be empty..")
    private String destination;

    private Integer noOfSeatsBooked;
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

    @ManyToOne
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

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
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
                        LocalDate reservationDate, LocalDate journeyDate, LocalTime reservationTime,
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
        this.reservationTime = reservationTime;
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
