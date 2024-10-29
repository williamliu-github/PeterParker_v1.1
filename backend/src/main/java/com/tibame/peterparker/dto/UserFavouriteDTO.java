package com.tibame.peterparker.dto;

public class UserFavouriteDTO {
    private Integer userFavouriteId;

    private Integer userId;

    private Integer parkingId;

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserFavouriteId() {
        return userFavouriteId;
    }

    public void setUserFavouriteId(Integer userFavouriteId) {
        this.userFavouriteId = userFavouriteId;
    }

    public UserFavouriteDTO(Integer userFavouriteId, Integer userId, Integer parkingId) {
        this.userFavouriteId = userFavouriteId;
        this.userId = userId;
        this.parkingId = parkingId;
    }

    public UserFavouriteDTO() {}
}
