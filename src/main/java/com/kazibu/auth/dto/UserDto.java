package com.kazibu.auth.dto;

public class UserDto {

  // 通用用户请求类，支持增删改查
  public static class UserRequest {
    private Long id; // 编辑和删除时需要
    private Boolean enabled; // 编辑时需要
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

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }
  }

  // 用户响应类，包含角色信息
  public static class UserResponse {
    private Long id;
    private String username;
    private Boolean enabled;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;
    private java.util.List<RoleInfo> roles;

    // 角色信息类
    public static class RoleInfo {
      private Long id;
      private String name;
      private String description;

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

    public java.time.LocalDateTime getCreateTime() {
      return createTime;
    }

    public void setCreateTime(java.time.LocalDateTime createTime) {
      this.createTime = createTime;
    }

    public java.time.LocalDateTime getUpdateTime() {
      return updateTime;
    }

    public void setUpdateTime(java.time.LocalDateTime updateTime) {
      this.updateTime = updateTime;
    }

    public java.util.List<RoleInfo> getRoles() {
      return roles;
    }

    public void setRoles(java.util.List<RoleInfo> roles) {
      this.roles = roles;
    }
  }
}