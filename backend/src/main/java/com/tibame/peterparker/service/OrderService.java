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
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    private OrderMailService orderMailService;

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
        return orderRepository.findByUser_UserId(userId);
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

            // 查找 Space
            Space space = spaceRepository.findById(orderDTO.getSpaceId())
                    .orElseThrow(() -> new EntityNotFoundException("Space not found"));
            order.setSpace(space);  // 設置 Space

            //設置訂單相關訊息
            order.setStatusId(orderDTO.getStatusId());
            order.setUserComment(orderDTO.getUserComment());
            order.setOrderStartTime(orderDTO.getOrderStartTime());
            order.setOrderEndTime(orderDTO.getOrderEndTime());
            order.setOrderTotalIncome(orderDTO.getOrderTotalIncome());
            order.setOrderModified(new Timestamp(System.currentTimeMillis()));

            //保存訂單
            OrderVO savedOrder = orderRepository.save(order);

            //發送郵件通知
            String userAccount = user.getUserAccount(); // 獲取 userAccount
            String mailText = generateMailText(savedOrder); // 根據訂單生成郵件內容
            orderMailService.sendMail(userAccount, mailText); // 調用郵件服務發送通知

            return savedOrder.getOrderId();
        } else {
            throw new IllegalStateException("The selected space is not available for the chosen time period.");
        }
    }

    //訂單完成通知內容
    private String generateMailText(OrderVO order) {
        return "親愛的用戶，您好，您的訂單已經完成，以下是您的訂單詳情：\n" +
                "訂單編號: " + order.getOrderId() + "\n" +
                "停車場: " + order.getSpace().getParkingInfo().getParkingName() + "\n" +
                "開始時間: " + order.getOrderStartTime() + "\n" +
                "結束時間: " + order.getOrderEndTime() + "\n" +
                "總金額: " + order.getOrderTotalIncome() + " NTD\n" +
                "感謝您的使用！";
    }



    // 更新訂單
    public OrderVO updateOrder(OrderVO updatedOrder) {
        Optional<OrderVO> existingOrderOpt = orderRepository.findById(updatedOrder.getOrderId());
        if (existingOrderOpt.isPresent()) {
            OrderVO existingOrder = existingOrderOpt.get();

            // 查找 Space
            Space space = spaceRepository.findById(updatedOrder.getSpace().getSpaceId())
                    .orElseThrow(() -> new EntityNotFoundException("Space not found"));
            existingOrder.setSpace(space);  // 設置 Space

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
    public Integer calculateTotalPrice(OrderVO request) {
        // 檢查是否有空值
        if (request.getOrderStartTime() == null) {
            throw new IllegalArgumentException("訂單開始時間不可為null");
        }

        // 如果訂單狀態為 "已取消"，設置 orderTotalIncome 為 0 並保存，然後拋出異常
        if ("已取消".equals(request.getStatusId())) {
            request.setOrderTotalIncome(0);
            orderRepository.save(request);
            throw new IllegalArgumentException("訂單未成立或已取消");
        }

        // 如果 orderEndTime 為 null，設置預設結束時間
        if (request.getOrderEndTime() == null) {
            // 設置 orderEndTime 為開始時間後 1 小時，並取整點
            Timestamp defaultEndTime = new Timestamp(
                    request.getOrderStartTime().toLocalDateTime().plusHours(1).withMinute(0).withSecond(0).withNano(0)
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            // 如果 defaultEndTime 早於當前時間，則設置為當前時間後的一小時，並取整點
            Timestamp currentPlusOneHour = new Timestamp(
                    LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0)
                            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            // 如果 defaultEndTime 早於當前時間，則使用 currentPlusOneHour，否則使用 defaultEndTime
            request.setOrderEndTime(
                    defaultEndTime.before(new Timestamp(System.currentTimeMillis())) ? currentPlusOneHour : defaultEndTime);
        }

        // 從 OrderVO 中獲取 space 對象
        Space space = request.getSpace();
        if (space == null) {
            throw new EntityNotFoundException("並未找到此訂單的車位資訊");
        }

        // 獲取 space 所屬的 ParkingInfo
        ParkingVO parkingInfo = space.getParkingInfo();
        if (parkingInfo == null) {
            throw new EntityNotFoundException("並未找到此訂單的停車場資訊");
        }

        // 使用 orderStartTime 轉換為 LocalDate
        LocalDate orderDate = request.getOrderStartTime().toLocalDateTime().toLocalDate();

        // 判斷是否為週末
        boolean isHoliday = orderDate.getDayOfWeek() == DayOfWeek.SATURDAY || orderDate.getDayOfWeek() == DayOfWeek.SUNDAY;

        // 根據是否是假日選擇價格
        int pricePerHour = isHoliday ? parkingInfo.getHolidayHourlyRate() : parkingInfo.getWorkdayHourlyRate();

        // 計算訂單持續時間（小時）
        long durationInHours = (request.getOrderEndTime().getTime() - request.getOrderStartTime().getTime()) / (1000 * 60 * 60);
        if (durationInHours <= 0) {
            throw new IllegalArgumentException("訂單結束時間不可早於訂單開始時間");
        }

        // 計算訂單總金額
        Integer totalPrice = Math.toIntExact(pricePerHour * durationInHours);

        // 更新訂單總金額
        request.setOrderTotalIncome(totalPrice);
        orderRepository.save(request);

        // 返回計算的總金額
        return totalPrice;
    }

    //根據userId查找userAccount
    public String getUserAccountByUserId(Integer userId) {
        Optional<UserVO> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getUserAccount();
        } else {
            throw new EntityNotFoundException("User not found for ID: " + userId);
        }
    }



}
