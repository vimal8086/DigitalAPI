package com.one.digitalapi.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;


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

    @NotNull(message = "This Field can not be null..")
    private String source;

    @NotNull(message = "This Field can not be null..")
    private String destination;

    private Integer noOfSeatsBooked;

    private Integer fare;

    private String cancellationReason;  // New Field

    private Integer refundAmount;       // New Field

    @ManyToOne
    @JoinColumn( name = "bus_id", referencedColumnName = "busId")
    private Bus bus;

    @ManyToOne
    @JoinColumn( name = "user_id", referencedColumnName = "userId")
    private User user;


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

    public Reservations(Integer reservationId, String reservationStatus,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String reservationType,
                        LocalDate reservationDate, LocalDate journeyDate, LocalTime reservationTime,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String source,
                        @NotNull(message = "This Field can not be null..") @NotBlank(message = "This Field can not be blank..") @NotEmpty(message = "This Field can not be empty..") String destination,
                        Integer noOfSeatsBooked, Integer fare, Bus bus, User user, String cancellationReason, Integer refundAmount) {
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
    }


    public Reservations() {
        // TODO Auto-generated constructor stub
    }

}

