package com.tibame.peterparker.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserProfileDTO {
    private Integer userId;
    private MultipartFile profilePhoto;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public MultipartFile getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(MultipartFile profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public UserProfileDTO() {}

    public UserProfileDTO(Integer userId, MultipartFile profilePhoto) {}

}
