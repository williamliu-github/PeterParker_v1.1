package com.tibame.peterparker.service;

import com.tibame.peterparker.dao.ParkingRepository;
import com.tibame.peterparker.entity.ParkingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository ParkingRepository;

    // 獲取所有停車場資訊
    public List<ParkingVO> getAllParkingInfos() {
        return ParkingRepository.findAll();
    }

    // 根據ID獲取停車場資訊
    public Optional<ParkingVO> getParkingInfoById(Integer parkingId) {
        return ParkingRepository.findById(parkingId);
    }

    // 保存或更新停車場資訊
    public ParkingVO saveOrUpdateParkingInfo(ParkingVO parkingInfo) {
        return ParkingRepository.save(parkingInfo);
    }

    // 根據ID刪除停車場資訊
    public void deleteParkingInfoById(Integer parkingId) {
        ParkingRepository.deleteById(parkingId);
    }

    // 根據經緯度查找附近的停車場
    public List<Map<String, Object>> findNearbyParking(Double latitude, Double longitude) {
        // 實現查找附近停車場的邏輯（這裡可以根據實際邏輯篩選附近停車場）
        List<ParkingVO> parkingInfos = ParkingRepository.findAll(); // 佔位符

        // 將 ParkingInfo 轉換為 Map<String, Object>
        return parkingInfos.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    // 根據關鍵字查找停車場
    public List<Map<String, Object>> searchParkingByKeyword(String keyword) {
        // 實現根據關鍵字查找停車場的邏輯（這裡可以根據關鍵字進行篩選）
        List<ParkingVO> parkingInfos = ParkingRepository.findAll(); // 佔位符

        // 將 ParkingInfo 轉換為 Map<String, Object>
        return parkingInfos.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    // 獲取指定停車場的可用車位數量
    public Integer getAvailableSpaces(Integer parkingId) {
        // 實現獲取可用車位的邏輯
        return 0; // 佔位符
    }

    // 將 ParkingInfo 轉換為 Map<String, Object>
    private Map<String, Object> convertToMap(ParkingVO parkingInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("parkingId", parkingInfo.getParkingId());
        map.put("capacity", parkingInfo.getCapacity());
        map.put("parkingName", parkingInfo.getParkingName());
        map.put("parkingRegion", parkingInfo.getParkingRegion());
        map.put("parkingLocation", parkingInfo.getParkingLocation());
        map.put("holidayHourlyRate", parkingInfo.getHolidayHourlyRate());
        map.put("workdayHourlyRate", parkingInfo.getWorkdayHourlyRate());
        return map;
    }
}
