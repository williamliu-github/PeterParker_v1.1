package com.tibame.peterparker.service;

import com.tibame.peterparker.dao.OrderRepository;
import com.tibame.peterparker.dao.ParkingInfoRepository;
import com.tibame.peterparker.dao.SpaceRepository;
import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.entity.ParkingInfo;
import com.tibame.peterparker.entity.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ParkingInfoRepository parkingInfoRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    // 查找附近的停車場
    public List<ParkingInfo> findNearbyParking(Double lat, Double lng, Double radius) {
        return parkingInfoRepository.findByParkingLatBetweenAndParkingLongBetween(lat - radius, lat + radius, lng - radius, lng + radius);
    }

    // 查找指定停車場的可用車位
    public List<Space> findAvailableSpaces(Integer parkingId, Timestamp startTime, Timestamp endTime) {
        List<Space> spaces = spaceRepository.findByParkingInfoParkingId(parkingId);
        return spaces.stream()
                .filter(space -> {
                    List<OrderVO> conflictingOrders = orderRepository.findConflictingOrders(space.getSpaceId(), startTime, endTime);
                    return conflictingOrders.isEmpty();
                }).toList();
    }

    // 創建訂單
    public OrderVO createOrder(OrderVO order) {
        // 先檢查該車位在指定時段是否可用
        List<OrderVO> conflictingOrders = orderRepository.findConflictingOrders(order.getSpaceId(), order.getOrderStartTime(), order.getOrderEndTime());
        if (conflictingOrders.isEmpty()) {
            return orderRepository.save(order);
        } else {
            throw new IllegalStateException("The selected space is not available for the chosen time period.");
        }
    }
}