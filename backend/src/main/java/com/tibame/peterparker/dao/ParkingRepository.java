package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.ParkingVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingVO, Integer> {

    // 新增根據經緯度範圍查找停車場的方法
    List<ParkingVO> findByParkingLatBetweenAndParkingLongBetween(Double latStart, Double latEnd, Double longStart, Double longEnd);

    @Query("SELECT p FROM ParkingVO p WHERE (:types IS NULL)")
    List<ParkingVO> findFilteredParkingListings(@Param("types") List<String> types);

    @Query("SELECT p FROM ParkingVO p WHERE LOWER(p.parkingName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.parkingLocation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ParkingVO> searchByKeyword(@Param("keyword") String keyword);

}
