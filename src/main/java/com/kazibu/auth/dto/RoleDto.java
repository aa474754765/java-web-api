package com.kazibu.auth.dto;

public class RoleDto {

  // 通用角色请求类，支持增删改查
  public static class RoleRequest {
    private Long id; // 编辑和删除时需要
    private String name; // 新增和编辑时需要
    private String description; // 新增和编辑时需要

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
  }

  // 查询角色请求类
  public static class RoleQuery {
    private String name; // 按角色名模糊查询

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}