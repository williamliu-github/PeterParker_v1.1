package com.tibame.peterparker.dto;

public class UserUpdatePasswordDTO {

    private String updatePasswordToken;

    private String userPassword;

    public String getUpdatePasswordToken() {
        return updatePasswordToken;
    }

    public void setUpdatePasswordToken(String updatePasswordToken) {
        this.updatePasswordToken = updatePasswordToken;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserUpdatePasswordDTO(String updatePasswordToken, String userPassword) {
        this.updatePasswordToken = updatePasswordToken;
        this.userPassword = userPassword;
    }

    public UserUpdatePasswordDTO() {}
}
