package com.tibame.peterparker.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "owner")  // 對應數據庫中的 "owner" 表
public class Owner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 主鍵自動生成
    @Column(name = "ownerNo")  // 對應表中的 "ownerNo" 列
    private Integer ownerNo;

    @Column(name = "ownerAccount", nullable = false, length = 20)  // 帳號欄位，不能為空
    private String ownerAccount;

    @Column(name = "ownerPassword", nullable = false, length = 20)  // 密碼欄位，不能為空
    private String ownerPassword;

    @Column(name = "ownerName", length = 10)  // 姓名欄位
    private String ownerName;

    @Column(name = "ownerPhone", length = 20)  // 電話欄位
    private String ownerPhone;

    @Column(name = "ownerGoogleToken", length = 20)  // Google Token 欄位
    private String ownerGoogleToken;

    // 無參數構造函數
    public Owner() {}

    // 有參數構造函數
    public Owner(String ownerAccount, String ownerPassword) {
        this.ownerAccount = ownerAccount;
        this.ownerPassword = ownerPassword;
    }

    // Getter 和 Setter 方法
    public Integer getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(Integer ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerGoogleToken() {
        return ownerGoogleToken;
    }

    public void setOwnerGoogleToken(String ownerGoogleToken) {
        this.ownerGoogleToken = ownerGoogleToken;
    }
}
