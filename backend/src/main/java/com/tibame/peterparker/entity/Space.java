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

    @Column(name = "spaceNo", length = 5)
    private String spaceNo;

    @ManyToOne
    @JoinColumn(name = "parkingId", nullable = false)
    private ParkingVO parkingInfo;

    // Constructors
    public Space() {}

    public Space(ParkingVO parkingInfo) {
        this.parkingInfo = parkingInfo;
    }

    // Getters and Setters
    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public ParkingVO getParkingInfo() {
        return parkingInfo;
    }

    public void setParkingInfo(ParkingVO parkingInfo) {
        this.parkingInfo = parkingInfo;
    }

    public Integer getParkingId() {
        if (parkingInfo != null) {
            return parkingInfo.getParkingId();
        }
        return null;
    }

    public void setParkingId(Integer parkingId) {
        if (parkingInfo == null) {
            parkingInfo = new ParkingVO();
        }
        parkingInfo.setParkingId(parkingId);
    }

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    @Override
    public String toString() {
        return "Space{" +
                "spaceId=" + spaceId +
                ", parkingId=" + getParkingId() +
                ", spaceNo='" + spaceNo + '\'' +
                '}';
    }
}
