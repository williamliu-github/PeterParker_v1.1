package com.tibame.peterparker.dto;

public class UserBlacklistParkingDTO {

    private Integer userBlacklistId;

    private Integer parkingId;

    private Integer capacity;

    private String parkingName;

    private String parkingRegion;

    private String parkingLocation;

    private Double parkingLong;

    private Double parkingLat;

    private Integer holidayHourlyRate;

    private Integer workdayHourlyRate;

    private Integer ownerNo;

    public Integer getUserBlacklistId() {
        return userBlacklistId;
    }

    public void setUserBlacklistId(Integer userBlacklistId) {
        this.userBlacklistId = userBlacklistId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingRegion() {
        return parkingRegion;
    }

    public void setParkingRegion(String parkingRegion) {
        this.parkingRegion = parkingRegion;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Double getParkingLong() {
        return parkingLong;
    }

    public void setParkingLong(Double parkingLong) {
        this.parkingLong = parkingLong;
    }

    public Double getParkingLat() {
        return parkingLat;
    }

    public void setParkingLat(Double parkingLat) {
        this.parkingLat = parkingLat;
    }

    public Integer getHolidayHourlyRate() {
        return holidayHourlyRate;
    }

    public void setHolidayHourlyRate(Integer holidayHourlyRate) {
        this.holidayHourlyRate = holidayHourlyRate;
    }

    public Integer getWorkdayHourlyRate() {
        return workdayHourlyRate;
    }

    public void setWorkdayHourlyRate(Integer workdayHourlyRate) {
        this.workdayHourlyRate = workdayHourlyRate;
    }

    public Integer getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(Integer ownerNo) {
        this.ownerNo = ownerNo;
    }

    public UserBlacklistParkingDTO(Integer userBlacklistId, Integer parkingId, Integer capacity, String parkingName, String parkingRegion, String parkingLocation, Double parkingLong, Double parkingLat, Integer holidayHourlyRate, Integer workdayHourlyRate, Integer ownerNo) {
        this.userBlacklistId = userBlacklistId;
        this.parkingId = parkingId;
        this.capacity = capacity;
        this.parkingName = parkingName;
        this.parkingRegion = parkingRegion;
        this.parkingLocation = parkingLocation;
        this.parkingLong = parkingLong;
        this.parkingLat = parkingLat;
        this.holidayHourlyRate = holidayHourlyRate;
        this.workdayHourlyRate = workdayHourlyRate;
        this.ownerNo = ownerNo;
    }

    public UserBlacklistParkingDTO() {}
}
