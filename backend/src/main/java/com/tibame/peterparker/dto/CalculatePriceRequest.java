package com.tibame.peterparker.dto;

import java.sql.Timestamp;

public class CalculatePriceRequest {
    private Integer parkingId;
    private Timestamp orderStartTime;
    private Timestamp orderEndTime;

    // Getters and Setters
    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Timestamp getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(Timestamp orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public Timestamp getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(Timestamp orderEndTime) {
        this.orderEndTime = orderEndTime;
    }
}

