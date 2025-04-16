package com.one.digitalapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reservation")
public class ReservationProperties {

    private Double gstPercentage;

    public Double getGstPercentage() {
        return gstPercentage;
    }

    public void setGstPercentage(Double gstPercentage) {
        this.gstPercentage = gstPercentage;
    }
}
