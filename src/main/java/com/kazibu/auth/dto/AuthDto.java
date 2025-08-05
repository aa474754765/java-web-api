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
}