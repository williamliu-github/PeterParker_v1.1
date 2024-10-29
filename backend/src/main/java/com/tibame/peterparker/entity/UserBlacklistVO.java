package com.tibame.peterparker.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userBlacklist")
public class UserBlacklistVO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name= "userBlacklistId")
    private int userBlacklistId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)  // Use the actual column name in the DB
    private UserVO user;

    @ManyToOne
    @JoinColumn(name = "parkingId", nullable = false)  // Ensure this matches your DB column name
    private ParkingVO parking;

    public int getUserBlacklistId() {
        return userBlacklistId;
    }

    public void setUserBlacklistId(int userBlacklistId) {
        this.userBlacklistId = userBlacklistId;
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

    public UserBlacklistVO(int userBlacklistId, UserVO user, ParkingVO parking) {
        this.userBlacklistId = userBlacklistId;
        this.user = user;
        this.parking = parking;
    }

    public UserBlacklistVO() {}
}
