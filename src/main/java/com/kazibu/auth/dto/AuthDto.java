package com.kazibu.auth.dto;

// 登录请求类
public class AuthDto {
  public static class LoginRequest {
    private String username;
    private String password;

    // getter和setter
    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  // 注册请求类
  public static class RegisterRequest {
    private String username;
    private String password;
    private String nickName; // 用户昵称，非必填
    private String phoneNumber; // 手机号码，非必填

    // getter和setter
    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getNickName() {
      return nickName;
    }

    public void setNickName(String nickName) {
      this.nickName = nickName;
    }

    public String getPhoneNumber() {
      return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
    }
  }

  // 重置密码请求类
  public static class ResetPasswordRequest {
    private String username; // 用户名
    private String currentPassword; // 当前密码
    private String newPassword; // 新密码

    // getter和setter
    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getCurrentPassword() {
      return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
      this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
      return newPassword;
    }

    public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
    }
  }

  // 微信小程序登录请求类
  public static class WeChatLoginRequest {
    private String code; // 微信登录凭证code
    private String nickName; // 微信昵称（可选）
    private String avatarUrl; // 微信头像URL（可选）

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getNickName() {
      return nickName;
    }

    public void setNickName(String nickName) {
      this.nickName = nickName;
    }

    public String getAvatarUrl() {
      return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
      this.avatarUrl = avatarUrl;
    }
  }
}