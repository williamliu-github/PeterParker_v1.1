package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.OrderVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderVO, Integer> {

    // 根據狀態查找訂單
    List<OrderVO> findByStatusId(String statusId);

    // 根據用戶 ID 查找所有訂單
    List<OrderVO> findByUserUserId(Integer userId);

    // 查找在指定時間段內已被預訂的停車位
    @Query(value = "SELECT o FROM OrderVO o WHERE o.spaceId = :spaceId AND " +
            "((o.orderStartTime <= :startTime AND o.orderEndTime >= :startTime) OR " +
            "(o.orderStartTime <= :endTime AND o.orderEndTime >= :endTime) OR " +
            "(o.orderStartTime >= :startTime AND o.orderEndTime <= :endTime))")
    List<OrderVO> findConflictingOrders(Integer spaceId, Timestamp startTime, Timestamp endTime);

    // 修改訂單狀態
    @Modifying
    @Query(value = "UPDATE orderinfo o SET o.statusId = :statusId WHERE o.orderId = :orderId", nativeQuery = true)
    int updateOrderStatus(Integer orderId, String statusId);

    // 插入新的訂單
    @Modifying
    @Query(value = "INSERT INTO orderinfo (userId, spaceId, statusId, orderCheck, userComment, " +
            "orderStartTime, orderEndTime, orderTotalPrice, orderModified) " +
            "VALUES (:userId, :spaceId, :statusId, :orderCheck, :userComment, :orderStartTime, :orderEndTime, :orderTotalPrice, :orderModified)", nativeQuery = true)
    int insertOrder(Integer userId, Integer spaceId, String statusId, Boolean orderCheck, String userComment,
                    Timestamp orderStartTime, Timestamp orderEndTime, Integer orderTotalPrice, Timestamp orderModified);

    // 修改訂單信息
    @Modifying
    @Query(value = "UPDATE orderinfo SET userId = :userId, spaceId = :spaceId, statusId = :statusId, " +
            "orderCheck = :orderCheck, userComment = :userComment, orderStartTime = :orderStartTime, " +
            "orderEndTime = :orderEndTime, orderTotalPrice = :orderTotalPrice, orderModified = :orderModified " +
            "WHERE orderId = :orderId", nativeQuery = true)
    int updateOrder(Integer orderId, Integer userId, Integer spaceId, String statusId, Boolean orderCheck,
                    String userComment, Timestamp orderStartTime, Timestamp orderEndTime, Integer orderTotalPrice, Timestamp orderModified);
}
