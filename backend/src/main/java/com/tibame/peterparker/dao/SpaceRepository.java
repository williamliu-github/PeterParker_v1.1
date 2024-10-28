package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Integer> {
    List<Space> findByParkingInfoParkingId(Integer parkingId);
}

