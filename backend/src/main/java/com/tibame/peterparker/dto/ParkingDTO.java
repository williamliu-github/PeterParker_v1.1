package com.tibame.peterparker.dto;

public class ParkingDTO {
    private Integer parkingId; // 停車場 ID
    private Integer capacity; // 停車場總車位 I
    private String parkingName; // 停車場名稱
    private String parkingType; // 停車場類型（例如 "地下停車場"、"平面停車場"）
    private String parkingRegion;// 停車場地區
    private String parkingLocation; // 停車場地址
    private Double parkingLat; // 停車場緯度
    private Double parkingLong; // 停車場經度
    private Integer workdayHourlyRate; // 平日每小時收費
    private Integer holidayHourlyRate; // 假日每小時收費
    private String parkingImg; // 停車場圖片 URL
    private Double rating; // 評分
    private Integer reviewCount; // 評論數量
    private Integer availableSpaces; // 可用車位數量

    // Getters and Setters

    public String getParkingRegion() {
        return parkingRegion;
    }

    public void setParkingRegion(String parkingRegion) {
        this.parkingRegion = parkingRegion;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getParkingLocation() {
        return parkingLocation;
    }

    public void setParkingLocation(String parkingLocation) {
        this.parkingLocation = parkingLocation;
    }

    public Double getParkingLat() {
        return parkingLat;
    }

    public void setParkingLat(Double parkingLat) {
        this.parkingLat = parkingLat;
    }

    public Double getParkingLong() {
        return parkingLong;
    }

    public void setParkingLong(Double parkingLong) {
        this.parkingLong = parkingLong;
    }

    public Integer getWorkdayHourlyRate() {
        return workdayHourlyRate;
    }

    public void setWorkdayHourlyRate(Integer workdayHourlyRate) {
        this.workdayHourlyRate = workdayHourlyRate;
    }

    public Integer getHolidayHourlyRate() {
        return holidayHourlyRate;
    }

    public void setHolidayHourlyRate(Integer holidayHourlyRate) {
        this.holidayHourlyRate = holidayHourlyRate;
    }

    public String getParkingImg() {
        return parkingImg;
    }

    public void setParkingImg(String parkingImg) {
        this.parkingImg = parkingImg;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getAvailableSpaces() {
        return availableSpaces;
    }

    public void setAvailableSpaces(Integer availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
