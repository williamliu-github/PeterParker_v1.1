package com.tibame.peterparker.dto;

public class UserLoginRequestDTO {

    private String userAccount;
    private String userPassword;
    private boolean rememberMe;


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

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public UserLoginRequestDTO() {
        super();
    }

    public UserLoginRequestDTO(String userAccount, String userPassword, boolean rememberMe) {
    }


}
