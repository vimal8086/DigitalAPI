package com.one.digitalapi.dto;

import java.util.List;

public class BookedSeatDTO {
    private Integer busId;
    private List<String> seatName;

    // Constructor
    public BookedSeatDTO(Integer busId, List<String> seatName) {
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

    public List<String> getSeatName() {
        return seatName;
    }

    public void setSeatName(List<String> seatName) {
        this.seatName = seatName;
    }
}