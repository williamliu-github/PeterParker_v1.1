package com.tibame.peterparker.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userFavourite")
public class UserFavouriteVO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name= "userFavouriteId")
    private int userFavouriteId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)  // Use the actual column name in the DB
    private UserVO user;

    @ManyToOne
    @JoinColumn(name = "parkingId", nullable = false)  // Ensure this matches your DB column name
    private ParkingVO parking;

    public int getUserFavouriteId() {
        return userFavouriteId;
    }

    public void setUserFavouriteId(int userFavouriteId) {
        this.userFavouriteId = userFavouriteId;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public ParkingVO getParking() {
        return parking;
    }

    public void setParking(ParkingVO parking) {
        this.parking = parking;
    }

    public UserFavouriteVO(int userFavouriteId, UserVO user, ParkingVO parking) {
        this.userFavouriteId = userFavouriteId;
        this.user = user;
        this.parking = parking;
    }

    public UserFavouriteVO() {}
}
