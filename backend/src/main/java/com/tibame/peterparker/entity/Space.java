package com.tibame.peterparker.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "space")
public class Space implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spaceId")
    private Integer spaceId;

    @ManyToOne
    @JoinColumn(name = "parkingId", nullable = false)
    private ParkingInfo parkingInfo;

    // Constructors
    public Space() {}

    public Space(ParkingInfo parkingInfo) {
        this.parkingInfo = parkingInfo;
    }

    // Getters and Setters
    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public ParkingInfo getParkingInfo() {
        return parkingInfo;
    }

    public void setParkingInfo(ParkingInfo parkingInfo) {
        this.parkingInfo = parkingInfo;
    }
}