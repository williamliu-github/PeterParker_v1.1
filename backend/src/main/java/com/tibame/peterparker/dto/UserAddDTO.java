package com.tibame.peterparker.dto;

public class UserAddDTO {
    private String userName;
    private String userPhone;
    private String userAccount;
    private String userPassword;
    private String carNumber;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public UserAddDTO(String userName, String userPhone, String userAccount, String userPassword, String carNumber) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.carNumber = carNumber;
    }

    public UserAddDTO() {}
}
