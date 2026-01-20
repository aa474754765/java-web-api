package com.kazibu.auth.dto;

public class UserDto {

  // 通用用户请求类，支持增删改查
  public static class UserRequest {
    private Long id; // 编辑和删除时需要
    private Boolean enabled; // 编辑时需要
    private String nickName; // 用户昵称
    private String phoneNumber; // 手机号码
    private java.util.List<Long> roleIds; // 编辑时需要，角色ID列表

    // getter和setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Boolean getEnabled() {
      return enabled;
    }

    public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
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

    public java.util.List<Long> getRoleIds() {
      return roleIds;
    }

    public void setRoleIds(java.util.List<Long> roleIds) {
      this.roleIds = roleIds;
    }
  }

  // 查询用户请求类
  public static class UserQuery {
    private String username; // 按用户名模糊查询
    private String nickName; // 按用户昵称模糊查询
    private String phoneNumber; // 按手机号码模糊查询
    private Boolean enabled; // 按启用状态查询

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
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

    public Boolean getEnabled() {
      return enabled;
    }

    public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
    }
  }

  // 用户响应类，包含角色信息
  public static class UserResponse {
    private Long id;
    private String username;
    private Boolean enabled;
    private String nickName;
    private String phoneNumber;
    private String createTime;
    private String updateTime;
    private java.util.List<RoleInfo> roles;

    // 角色信息类
    public static class RoleInfo {
      private Long id;
      private String name;
      private String description;
      private Boolean status;
      private String roleName;

      public Long getId() {
        return id;
      }

      public void setId(Long id) {
        this.id = id;
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public String getDescription() {
        return description;
      }

      public void setDescription(String description) {
        this.description = description;
      }

      public Boolean getStatus() {
        return status;
      }

      public void setStatus(Boolean status) {
        this.status = status;
      }

      public String getRoleName() {
        return roleName;
      }

      public void setRoleName(String roleName) {
        this.roleName = roleName;
      }
    }

    // getter和setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public Boolean getEnabled() {
      return enabled;
    }

    public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
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

    public String getCreateTime() {
      return createTime;
    }

    public void setCreateTime(String createTime) {
      this.createTime = createTime;
    }

    public String getUpdateTime() {
      return updateTime;
    }

    public void setUpdateTime(String updateTime) {
      this.updateTime = updateTime;
    }

    public java.util.List<RoleInfo> getRoles() {
      return roles;
    }

    public void setRoles(java.util.List<RoleInfo> roles) {
      this.roles = roles;
    }
  }

  // 重置密码请求类
  public static class ResetPasswordRequest {
    private String username; // 用户名
    private String currentPassword; // 当前密码
    private String newPassword; // 新密码

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
}