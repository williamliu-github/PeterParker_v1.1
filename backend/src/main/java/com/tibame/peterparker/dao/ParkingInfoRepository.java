package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.ParkingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingInfoRepository extends JpaRepository<ParkingInfo, Integer> {

    // 新增根據經緯度範圍查找停車場的方法
    List<ParkingInfo> findByParkingLatBetweenAndParkingLongBetween(Double latStart, Double latEnd, Double longStart, Double longEnd);
}
