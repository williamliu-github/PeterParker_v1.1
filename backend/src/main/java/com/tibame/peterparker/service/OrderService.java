package com.tibame.peterparker.service;

import com.tibame.peterparker.dao.OrderRepository;
import com.tibame.peterparker.dao.ParkingInfoRepository;
import com.tibame.peterparker.dao.SpaceRepository;
import com.tibame.peterparker.dto.OrderDTO;
import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.entity.ParkingInfo;
import com.tibame.peterparker.entity.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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

    // 查找指定用戶的所有訂單
    public List<OrderVO> findOrdersByUserId(Integer userId) {
        return orderRepository.findByUserUserId(userId);
    }

    // 查找指定狀態的訂單
    public List<OrderVO> findOrdersByStatusId(String statusId) {
        return orderRepository.findByStatusId(statusId);
    }

    // 根據訂單 ID 查詢訂單
    public Optional<OrderVO> findOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
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

    // 更新訂單
    public OrderVO updateOrder(OrderVO updatedOrder) {
        Optional<OrderVO> existingOrderOpt = orderRepository.findById(updatedOrder.getOrderId());
        if (existingOrderOpt.isPresent()) {
            OrderVO existingOrder = existingOrderOpt.get();
            existingOrder.setSpaceId(updatedOrder.getSpaceId());
            existingOrder.setStatusId(updatedOrder.getStatusId());
            existingOrder.setUserComment(updatedOrder.getUserComment());
            existingOrder.setOrderStartTime(updatedOrder.getOrderStartTime());
            existingOrder.setOrderEndTime(updatedOrder.getOrderEndTime());
            existingOrder.setOrderTotalIncome(updatedOrder.getOrderTotalIncome());
            existingOrder.setOrderModified(updatedOrder.getOrderModified());
            return orderRepository.save(existingOrder);
        } else {
            throw new EntityNotFoundException("Order not found");
        }
    }

    // 刪除訂單
    public void deleteOrderById(Integer orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new EntityNotFoundException("Order not found");
        }
    }

    // 計算訂單總金額
    public Double calculateTotalPrice(OrderDTO request) {
        Space space = spaceRepository.findById(request.getSpaceId())
                .orElseThrow(() -> new EntityNotFoundException("Space not found"));
        Double pricePerHour = space.getPricePerHour();
        long durationInHours = (request.getOrderEndTime().getTime() - request.getOrderStartTime().getTime()) / (1000 * 60 * 60);
        return pricePerHour * durationInHours;
    }
}
