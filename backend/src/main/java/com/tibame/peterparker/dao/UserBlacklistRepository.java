package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.UserBlacklistVO;
import com.tibame.peterparker.entity.UserFavouriteVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBlacklistRepository extends JpaRepository <UserBlacklistVO, Integer> {
        List<UserFavouriteVO> findByUserUserId(Integer userId);

        @Query(value = "SELECT ub.userBlacklistId, p.parkingId, p.capacity, p.parkingName, p.parkingRegion, p.parkingLocation, " +
                "p.parkingLong, p.parkingLat, p.holidayHourlyRate, p.workdayHourlyRate, p.ownerNo " +
                "FROM userBlacklist ub " +
                "JOIN parkinginfo p ON ub.parkingId = p.parkingId " +
                "WHERE ub.userId = :userId", nativeQuery = true)
        List<Object[]> findParkingByUserUserId(@Param("userId") Integer userId);


        @Query(value = "SELECT userBlacklistId FROM userBlacklist WHERE userId = :userId AND parkingId = :parkingId", nativeQuery = true)
        Integer findByUserIdAndParkingId(@Param("userId") Integer userId, @Param("parkingId") Integer parkingId);

        void deleteByUserBlacklistId(Integer userBlacklistId);
}
