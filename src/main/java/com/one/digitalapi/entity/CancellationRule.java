package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class CancellationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hoursBeforeDeparture;  // e.g., 24, 12, etc.

    private double refundPercentage;   // e.g., 100.0, 50.0, 0.0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    @JsonBackReference  //  Prevents infinite recursion
    private Bus bus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHoursBeforeDeparture() {
        return hoursBeforeDeparture;
    }

    public void setHoursBeforeDeparture(int hoursBeforeDeparture) {
        this.hoursBeforeDeparture = hoursBeforeDeparture;
    }

    public double getRefundPercentage() {
        return refundPercentage;
    }

    public void setRefundPercentage(double refundPercentage) {
        this.refundPercentage = refundPercentage;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}