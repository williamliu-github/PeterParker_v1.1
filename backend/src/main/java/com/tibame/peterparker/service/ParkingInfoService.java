package com.tibame.peterparker.service;

import com.tibame.peterparker.dto.ParkingInfoRequest;
import com.tibame.peterparker.entity.ParkingInfo;

import java.util.List;

public interface ParkingInfoService {

    ParkingInfo getParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId);
    Integer createParkingInfo(ParkingInfoRequest parkingInfoRequest);
    void updateParkingInfo(Integer ownerNo, Integer parkingId, ParkingInfoRequest parkingInfoRequest);
    void deleteParkingInfoByOwnerAndId(Integer ownerNo, Integer parkingId);
    List<ParkingInfo> getAllParkingInfoByOwnerNo(Integer ownerNo); // 查詢所有停車場的方法
    ParkingInfo getParkingInfoById(Integer parkingId);
    void saveParkingImage(Integer ownerNo, Integer parkingId, byte[] imageData);
    byte[] getParkingImage(Integer ownerNo, Integer parkingId); // 獲取圖片數據的方法


}