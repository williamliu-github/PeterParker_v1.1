package com.tibame.peterparker.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="user")
public class UserVO implements Serializable {

    //Field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Integer userId;

    @Column(name = "user_name", nullable = false)
    @JsonProperty("user_name")
    private String userName;

    @Column(name = "user_account", nullable = false)
    @Email
    @JsonProperty("user_account")
    private String userAccount;


    @Column(name = "user_password", nullable = false)
    @JsonProperty("user_password")
    private String userPassword;

    @Column(name = "user_phone", nullable = false)
    @JsonProperty("user_phone")
    private String userPhone;

    @Column(name = "car_number", nullable = false)
    @JsonProperty("car_number")
    private String carNumber;


    @Column(name = "google_token", nullable = true)
    @JsonProperty("google_token")
    private String googleToken;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<UserFavouritesVO> userFavourites;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public UserVO() {
    }

    public UserVO(String userName, String userAccount, String userPassword, String userPhone, String carNumber) {
        this.userName = userName;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.carNumber = carNumber;
    }

    public UserVO(int userId, String userName, String userAccount, String userPassword, String userPhone, String carNumber) {
        this.userName = userName;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.carNumber = carNumber;
    }
}