package com.tibame.peterparker.service;

import com.tibame.peterparker.dao.OrderRepository;
import com.tibame.peterparker.dao.ParkingRepository;
import com.tibame.peterparker.dao.SpaceRepository;
import com.tibame.peterparker.dto.FilterRequest;
import com.tibame.peterparker.dto.ParkingDTO;
import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.entity.ParkingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private OrderRepository orderRepository;

    // 獲取所有停車場資訊
    public List<ParkingVO> getAllParkingInfos() {
        return parkingRepository.findAll();
    }

    // 根據ID獲取停車場資訊
    public Optional<ParkingVO> getParkingInfoById(Integer parkingId) {
        return parkingRepository.findById(parkingId);
    }

    // 保存或更新停車場資訊
    public ParkingVO saveOrUpdateParkingInfo(ParkingVO parkingInfo) {
        return parkingRepository.save(parkingInfo);
    }

    // 根據ID刪除停車場資訊
    public void deleteParkingInfoById(Integer parkingId) {
        parkingRepository.deleteById(parkingId);
    }

    // 根據經緯度查找附近的停車場
    public List<Map<String, Object>> findNearbyParking(Double latitude, Double longitude) {
        // 實現查找附近停車場的邏輯（這裡可以根據實際邏輯篩選附近停車場）
        List<ParkingVO> parkingInfos = parkingRepository.findAll(); // 佔位符

        // 將 ParkingInfo 轉換為 Map<String, Object>
        return parkingInfos.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    // 根據關鍵字查找停車場
    public List<Map<String, Object>> searchParkingByKeyword(String keyword) {
        // 檢查是否有提供關鍵字
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be empty");
        }

        // 使用自定義查詢來查找符合關鍵字的停車場
        List<ParkingVO> parkingInfos = parkingRepository.searchByKeyword(keyword);

        // 將 ParkingInfo 轉換為 Map<String, Object>
        return parkingInfos.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    // 獲取指定停車場的可用車位數量
    public Integer getAvailableSpaces(Integer parkingId, Date date, String timeRange) {
        // 取得該停車場總車位數量
        Integer totalSpaces = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new EntityNotFoundException("ParkingInfo not found for id: " + parkingId))
                .getCapacity();

        // 將timeRange拆分為開始和結束時間
        String[] times = timeRange.split("-");
        if (times.length != 2) {
            throw new IllegalArgumentException("Invalid time range format. Expected 'HH:mm-HH:mm'");
        }
        Time startTime = Time.valueOf(times[0] + ":00");
        Time endTime = Time.valueOf(times[1] + ":00");

        // 找出指定日期和時間段內與該停車場有衝突的訂單
        List<OrderVO> conflictingOrders = orderRepository.findConflictingOrdersByParkingIdAndDate(parkingId, date, startTime, endTime);

        // 剩餘可用空位 = 總車位數量 - 已被預定的車位數量
        int availableSpaces = totalSpaces - conflictingOrders.size();

        return availableSpaces;
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

    //根據地圖邊界查找停車場
    public List<Map<String, Object>> findParkingByBounds(Double northEastLat, Double northEastLng, Double southWestLat, Double southWestLng) {

        List<ParkingVO> parkingInfos = parkingRepository.findByParkingLatBetweenAndParkingLongBetween(southWestLat, northEastLat, southWestLng, northEastLng);
        return parkingInfos.stream().map(this::convertToMap).collect(Collectors.toList());
    }

    // 根據經緯度和半徑查找停車場
    public List<Map<String, Object>> findNearbyParking(Double latitude, Double longitude, Double radius) {
        // 可以使用 Haversine 公式計算範圍內的點
        List<ParkingVO> parkingInfos = parkingRepository.findAll(); // 假設這裡是查找全部，再根據距離篩選
        return parkingInfos.stream()
                .filter(parking -> calculateDistance(latitude, longitude, parking.getParkingLat(), parking.getParkingLong()) <= radius)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int EARTH_RADIUS = 6371; // 地球半徑，單位：公里

        // 將緯度和經度從度數轉換為弧度
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 計算兩點間的距離
        return EARTH_RADIUS * c;
    }

    public List<ParkingDTO> getFilteredParkingListings(FilterRequest filterRequest) {
        // 依據過濾條件呼叫 ParkingRepository 的查詢方法
        List<ParkingVO> filteredParking = parkingRepository.findFilteredParkingListings(filterRequest.getTypes());

        // 將結果轉換為 ParkingDTO 並返回
        return filteredParking.stream().map(parking -> {
            ParkingDTO dto = new ParkingDTO();
            dto.setParkingId(parking.getParkingId());
            dto.setParkingName(parking.getParkingName());
            dto.setParkingLocation(parking.getParkingLocation());
            dto.setWorkdayHourlyRate(parking.getWorkdayHourlyRate());
            dto.setHolidayHourlyRate(parking.getHolidayHourlyRate());
            // 根據需要可以加入更多屬性
            return dto;
        }).collect(Collectors.toList());
    }



}
