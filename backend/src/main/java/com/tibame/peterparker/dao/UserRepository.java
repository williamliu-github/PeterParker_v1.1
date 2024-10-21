package com.tibame.peterparker.dao;

import com.tibame.peterparker.entity.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserVO, Integer> {
    boolean existsByUserAccount(String userAccount);
    UserVO findByUserAccountAndUserPassword(String userAccount, String userPassword);
    UserVO findByUserId(Integer userId);

    @Modifying
    @Query(value = "UPDATE user u SET u.user_password = :userPassword WHERE u.user_account = :userAccount", nativeQuery = true)
    int updateUserPassword(String userAccount, String userPassword);


    @Modifying
    @Query(value = "INSERT INTO user (user_name, user_account, user_password, user_phone, car_number) " +
            "VALUES (:userName, :userAccount, :userPassword, :userPhone, :carNumber)", nativeQuery = true)
    int insertUser(String userName, String userPhone, String userAccount, String userPassword, String carNumber);


    @Modifying
    @Query(value = "UPDATE user SET user_name = :userName, user_phone = :userPhone, user_account = :userAccount, " +
            "user_password = :userPassword, car_number = :carNumber WHERE user_id = :userId", nativeQuery = true)
    int updateUser(String userName, String userPhone,
                   String userAccount, String userPassword,
                   String carNumber, int userId);




}
