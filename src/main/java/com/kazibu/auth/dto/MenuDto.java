package com.kazibu.auth.dto;

public class MenuDto {

  // 通用菜单请求类，支持增删改查
  public static class MenuRequest {
    private Long id; // 编辑和删除时需要
    private String name; // 新增和编辑时需要
    private String title; // 菜单名称，新增和编辑时需要
    private String component; // 组件路径，新增和编辑时需要
    private String perms; // 权限，新增和编辑时需要
    private String visible; // 是否展示，新增和编辑时需要，默认为"1"
    private String isCache; // 是否缓存，新增和编辑时需要，默认为"0"
    private String menuType; // 菜单类型，新增和编辑时需要
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

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getComponent() {
      return component;
    }

    public void setComponent(String component) {
      this.component = component;
    }

    public String getPerms() {
      return perms;
    }

    public void setPerms(String perms) {
      this.perms = perms;
    }

    public String getVisible() {
      return visible;
    }

    public void setVisible(String visible) {
      this.visible = visible;
    }

    public String getIsCache() {
      return isCache;
    }

    public void setIsCache(String isCache) {
      this.isCache = isCache;
    }

    public String getMenuType() {
      return menuType;
    }

    public void setMenuType(String menuType) {
      this.menuType = menuType;
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

  // 查询菜单请求类
  public static class MenuQuery {
    private Long parentId;
    private String visible; // 按是否展示查询
    private String title; // 按菜单名称模糊查询

    public Long getParentId() {
      return parentId;
    }

    public void setParentId(Long parentId) {
      this.parentId = parentId;
    }

    public String getVisible() {
      return visible;
    }

    public void setVisible(String visible) {
      this.visible = visible;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }
}