package com.tibame.peterparker.dto;

public class UserAdditionalGoogleRegistrationDTO {
    private String userPhone;
    private String carNumber;

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

    public UserAdditionalGoogleRegistrationDTO(String userPhone, String carNumber) {
        this.userPhone = userPhone;
        this.carNumber = carNumber;
    }
}
