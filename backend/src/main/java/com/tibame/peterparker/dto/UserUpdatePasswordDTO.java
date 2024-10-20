package com.tibame.peterparker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserUpdatePasswordDTO {

    @JsonProperty("update_password_token")
    private String updatePasswordToken;

    @JsonProperty("user_password")
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
