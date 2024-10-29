package com.tibame.peterparker.service;

import com.tibame.peterparker.dao.OrderRepository;
import com.tibame.peterparker.dao.ParkingRepository;
import com.tibame.peterparker.dao.SpaceRepository;
import com.tibame.peterparker.dao.UserRepository;
import com.tibame.peterparker.dto.OrderDTO;
import com.tibame.peterparker.entity.OrderVO;
import com.tibame.peterparker.entity.ParkingVO;
import com.tibame.peterparker.entity.Space;
import com.tibame.peterparker.entity.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import java.time.DayOfWeek;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ParkingRepository ParkingRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserRepository userRepository;

    // 查找附近的停車場
    public List<ParkingVO> findNearbyParking(Double lat, Double lng, Double radius) {
        return ParkingRepository.findByParkingLatBetweenAndParkingLongBetween(lat - radius, lat + radius, lng - radius, lng + radius);
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
        return orderRepository.findByUserId(userId);
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
    public Integer createOrder(OrderDTO orderDTO) {
        // 先檢查該車位在指定時段是否可用
        List<OrderVO> conflictingOrders = orderRepository.findConflictingOrders(orderDTO.getSpaceId(), orderDTO.getOrderStartTime(), orderDTO.getOrderEndTime());
        if (conflictingOrders.isEmpty()) {
            OrderVO order = new OrderVO();

            // 查找 UserVO
            UserVO user = userRepository.findByUserId(orderDTO.getUserId());
            if (user == null) {
                throw new EntityNotFoundException("User not found");
            }
            order.setUser(user);  // 設置 UserVO

            order.setSpaceId(orderDTO.getSpaceId());
            order.setStatusId(orderDTO.getStatusId());
            order.setUserComment(orderDTO.getUserComment());
            order.setOrderStartTime(orderDTO.getOrderStartTime());
            order.setOrderEndTime(orderDTO.getOrderEndTime());
            order.setOrderTotalIncome(orderDTO.getOrderTotalIncome());
            order.setOrderModified(new Timestamp(System.currentTimeMillis()));

            OrderVO savedOrder = orderRepository.save(order);
            return savedOrder.getOrderId();
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
            existingOrder.setOrderModified(new Timestamp(System.currentTimeMillis()));
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
    public Integer calculateTotalPrice(OrderDTO request) {
        // 查找指定的 space
        Space space = spaceRepository.findById(request.getSpaceId())
                .orElseThrow(() -> new EntityNotFoundException("Space not found"));

        // 獲取 space 所屬的 ParkingInfo
        ParkingVO parkingInfo = space.getParkingInfo();

        // 使用 orderStartTime 轉換為 LocalDate
        LocalDate orderDate = request.getOrderStartTime().toLocalDateTime().toLocalDate();

        // 判斷是否為週末
        boolean isHoliday = orderDate.getDayOfWeek() == DayOfWeek.SATURDAY || orderDate.getDayOfWeek() == DayOfWeek.SUNDAY;

        // 根據是否是假日選擇價格
        int pricePerHour = isHoliday ? parkingInfo.getHolidayHourlyRate() : parkingInfo.getWorkdayHourlyRate();

        // 計算訂單持續時間（小時）
        long durationInHours = (request.getOrderEndTime().getTime() - request.getOrderStartTime().getTime()) / (1000 * 60 * 60);

        // 計算並返回訂單總金額
        return Math.toIntExact(pricePerHour * durationInHours);
    }


}
