package com.kazibu.auth.dto;

public class MenuDto {

  // 通用菜单请求类，支持增删改查
  public static class MenuRequest {
    private Long id; // 编辑和删除时需要
    private String name; // 新增和编辑时需要
    private String path; // 新增和编辑时需要
    private String icon; // 新增和编辑时需要
    private Long parentId; // 新增时需要
    private Integer sort; // 新增和编辑时需要

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

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public String getIcon() {
      return icon;
    }

    public void setIcon(String icon) {
      this.icon = icon;
    }

    public Long getParentId() {
      return parentId;
    }

    public void setParentId(Long parentId) {
      this.parentId = parentId;
    }

    public Integer getSort() {
      return sort;
    }

    public void setSort(Integer sort) {
      this.sort = sort;
    }
  }

  // 查询菜单请求类（只需要parentId）
  public static class MenuQuery {
    private Long parentId;

    public Long getParentId() {
      return parentId;
    }

    public void setParentId(Long parentId) {
      this.parentId = parentId;
    }
  }
}