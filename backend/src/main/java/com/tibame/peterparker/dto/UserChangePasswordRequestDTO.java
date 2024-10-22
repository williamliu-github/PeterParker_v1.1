package com.tibame.peterparker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserChangePasswordRequestDTO {

    private String userAccount;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public UserChangePasswordRequestDTO(String userAccount) {
        this.userAccount = userAccount;
    }

    public UserChangePasswordRequestDTO() {}
}
