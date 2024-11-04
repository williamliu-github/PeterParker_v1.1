package com.tibame.peterparker.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.tibame.peterparker.dto.FilterRequest;
import com.tibame.peterparker.dto.ParkingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tibame.peterparker.dto.OrderDTO;
import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.service.OrderService;
import com.tibame.peterparker.service.ParkingService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

import java.sql.Date;

@RestController
@RequestMapping(path = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ParkingService parkingService;

    // 查詢用戶所有訂單
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderVO>> getUserOrders(@PathVariable Integer userId) {
        List<OrderVO> orders = orderService.findOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 查詢訂單狀態
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<OrderVO>> getOrdersByStatus(@PathVariable String statusId) {
        List<OrderVO> orders = orderService.findOrdersByStatusId(statusId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 創建訂單
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        //使用者未登入則阻止成立
//        if (loginUserId == null) {
//            return new ResponseEntity<>("使用者未登入", HttpStatus.UNAUTHORIZED);
//        }

        // 如果用戶未登入，使用預設 ID 999
        if (loginUserId == null) {
            loginUserId = 1; // 預設的未登入用戶 ID
        }

        orderDTO.setUserId(loginUserId);


        try {
            Integer orderId = orderService.createOrder(orderDTO);
            return new ResponseEntity<>(orderId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 根據 orderId 查詢訂單
    @GetMapping("/{orderId}")
    public ResponseEntity<?> findById(@PathVariable Integer orderId) {
        Optional<OrderVO> optional = orderService.findOrderById(orderId);

        if (optional.isPresent()) {
            OrderVO order = optional.get();
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>("Order not found", HttpStatus.BAD_REQUEST);
    }

    // 訂單總金額計算***
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateTotalPrice(@RequestParam Integer orderId) {
        try {
            // 通過 orderId 獲取訂單的詳細資料
            Optional<OrderVO> order = orderService.findOrderById(orderId);
            if (order.isEmpty()) {
                return new ResponseEntity<>("並未找到此訂單: " + orderId, HttpStatus.NOT_FOUND);
            }

            // 使用獲取的訂單資料進行計算
            Integer totalPrice = orderService.calculateTotalPrice(order.get());
            return new ResponseEntity<>(Map.of("totalPrice", totalPrice), HttpStatus.OK);
        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException enfe) {
            return new ResponseEntity<>(enfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("在計算總金額時發生錯誤: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 根據經緯度查找附近的停車場
    @PostMapping("/nearbyParking")
    public ResponseEntity<?> getNearbyParking(@RequestBody Map<String, Object> request) {
        try {
            Double latitude = (Double) request.get("latitude");
            Double longitude = (Double) request.get("longitude");
            Double radius = (Double) request.get("radius");

            List<Map<String, Object>> nearbyParking = parkingService.findNearbyParking(latitude, longitude, radius);
            return new ResponseEntity<>(nearbyParking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error finding nearby parking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 用關鍵字查找停車場
    @GetMapping("/searchParking")
    @Lazy
    public ResponseEntity<?> searchParking(@RequestParam(value = "keyword", required = false) String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return new ResponseEntity<>("Keyword parameter is missing or invalid", HttpStatus.BAD_REQUEST);
            }

            List<Map<String, Object>> parkingResults = parkingService.searchParkingByKeyword(keyword);
            return new ResponseEntity<>(parkingResults, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error searching parking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 顯示停車場的剩餘可用車位數量
    @GetMapping("/availableSpaces/{parkingId}")
    @Lazy
    public ResponseEntity<?> getAvailableSpaces(
            @PathVariable Integer parkingId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date date,
            @RequestParam(value = "timeRange", required = false) String timeRange) {
        try {
            // 如果未傳入 date，則設置為當天日期
            if (date == null) {
                Calendar calendar = Calendar.getInstance();
                date = calendar.getTime();
            }

            // 將 java.util.Date 轉換為 java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            // 如果未傳入 timeRange，則設置為當下到三小時後
            if (timeRange == null || timeRange.isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String startTime = timeFormat.format(calendar.getTime());

                calendar.add(Calendar.HOUR_OF_DAY, 3);
                String endTime = timeFormat.format(calendar.getTime());

                timeRange = startTime + "-" + endTime; // 例如 "14:00-17:00"
            }

            // 調用 Service 的方法獲取結果
            Integer availableSpaces = parkingService.getAvailableSpaces(parkingId, sqlDate, timeRange);
            return new ResponseEntity<>(Map.of("availableSpaces", availableSpaces), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving available spaces: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/getParkingListings")
    public ResponseEntity<?> getParkingListings(@RequestBody Map<String, Object> bounds) {
        try {
            Double northEastLat = (Double) ((Map<String, Object>) bounds.get("northEast")).get("lat");
            Double northEastLng = (Double) ((Map<String, Object>) bounds.get("northEast")).get("lng");
            Double southWestLat = (Double) ((Map<String, Object>) bounds.get("southWest")).get("lat");
            Double southWestLng = (Double) ((Map<String, Object>) bounds.get("southWest")).get("lng");

            List<Map<String, Object>> parkingListings = parkingService.findParkingByBounds(northEastLat, northEastLng, southWestLat, southWestLng);
            return new ResponseEntity<>(parkingListings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching parking listings: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/filteredParkingListings")
    public ResponseEntity<?> getFilteredParkingListings(@RequestBody FilterRequest filterRequest) {
        try {
            List<ParkingDTO> parkingList = parkingService.getFilteredParkingListings(filterRequest);
            if (parkingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(parkingList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching filtered parking listings: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
