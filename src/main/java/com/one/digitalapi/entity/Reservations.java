package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DynamicUpdate
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

    private String orderId;

    private Double gstAmount;

    private Double totalAmount;

    private Double discountAmount;

    @ManyToOne(optional = true)
    @Valid
    @JoinColumn(name = "bus_id", referencedColumnName = "busId")
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // This prevents infinite recursion
    private List<Passenger> passengers;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

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

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
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

    // Constructor with new fields
    public Reservations(Integer reservationId, String reservationStatus,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String reservationType,
                        LocalDateTime reservationDate, LocalDateTime journeyDate,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String source,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String destination,
                        Integer noOfSeatsBooked, Integer fare, Bus bus, User user, String cancellationReason, Integer refundAmount,
                        String username, String mobileNumber, String email, String gender, Discount discount, String pickupAddress, String pickupTime, String dropAddress, String dropTime,
                        String orderId, Double gstAmount, Double totalAmount, Double discountAmount) {
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
        this.discount = discount;
        this.pickupAddress = pickupAddress;
        this.pickupTime = pickupTime;
        this.dropAddress = dropAddress;
        this.dropTime = dropTime;
        this.orderId = orderId;
        this.gstAmount = gstAmount;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
    }

    public Reservations() {
        // Default constructor
    }
}