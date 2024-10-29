package com.tibame.peterparker.dto;

public class UserBlacklistDTO {

    private Integer userBlacklistId;


    private Integer userId;

    private Integer parkingId;

    public Integer getUserBlacklistId() {
        return userBlacklistId;
    }

    public void setUserBlacklistId(Integer userBlacklistId) {
        this.userBlacklistId = userBlacklistId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public UserBlacklistDTO(Integer userBlacklistId, Integer userId, Integer parkingId) {
        this.userBlacklistId = userBlacklistId;
        this.userId = userId;
        this.parkingId = parkingId;
    }

    public UserBlacklistDTO() {}
}
