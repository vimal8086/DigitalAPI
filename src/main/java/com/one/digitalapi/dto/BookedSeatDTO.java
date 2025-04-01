package com.one.digitalapi.dto;

public class BookedSeatDTO {

    private Integer busId;
    private String seatName;

    // Constructor
    public BookedSeatDTO(Integer busId, String seatName) {
        this.busId = busId;
        this.seatName = seatName;
    }

    // Getters and Setters
    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }
}