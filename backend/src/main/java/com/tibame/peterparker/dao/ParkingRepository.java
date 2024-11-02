package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.ParkingVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingVO, Integer> {

    // 新增根據經緯度範圍查找停車場的方法
    List<ParkingVO> findByParkingLatBetweenAndParkingLongBetween(Double latStart, Double latEnd, Double longStart, Double longEnd);
}
