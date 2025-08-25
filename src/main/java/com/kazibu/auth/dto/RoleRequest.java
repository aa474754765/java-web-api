package com.kazibu.auth.dto;

import java.util.List;

// 通用角色请求类，支持增删改查
public class RoleRequest {
  private Long id; // 编辑和删除时需要
  private String name; // 新增和编辑时需要
  private String description; // 新增和编辑时需要
  private Boolean status; // 角色状态，新增和编辑时支持
  private String roleName; // 角色显示名称，新增和编辑时支持
  private List<Long> menuIds; // 菜单ID列表，新增和编辑时支持

  // getter和setter
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

  public List<Long> getMenuIds() {
    return menuIds;
  }

  public void setMenuIds(List<Long> menuIds) {
    this.menuIds = menuIds;
  }
}