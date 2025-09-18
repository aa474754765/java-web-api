package com.kazibu.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "menu")
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(name = "title", length = 100)
  private String title;

  @Column(name = "component", length = 100)
  private String component;

  @Column(name = "perms", length = 100)
  private String perms;

  @Column(name = "visible", nullable = false, length = 1)
  private String visible = "1";

  @Column(name = "is_cache", nullable = false, length = 1)
  private String isCache = "0";

  @Column(name = "menu_type", length = 100)
  private String menuType;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Menu parent;

  @Column(length = 200)
  private String path;

  @Column(length = 20)
  private String icon;

  @Column(name = "sort")
  private Integer sort;

  @Column(name = "create_time", updatable = false)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createTime = LocalDateTime.now();

  @Column(name = "update_time")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updateTime = LocalDateTime.now();

  @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JsonIgnore
  private java.util.List<RoleMenu> roleMenus = new java.util.ArrayList<>();

  // 构造函数
  public Menu() {
  }

  public Menu(String name, Menu parent, String path, String icon, Integer sort) {
    this.name = name;
    this.parent = parent;
    this.path = path;
    this.icon = icon;
    this.sort = sort;
  }

  public Menu(String name, String title, String component, String perms, String visible, String isCache,
      String menuType, Menu parent,
      String path,
      String icon, Integer sort) {
    this.name = name;
    this.title = title;
    this.component = component;
    this.perms = perms;
    this.visible = visible;
    this.isCache = isCache;
    this.menuType = menuType;
    this.parent = parent;
    this.path = path;
    this.icon = icon;
    this.sort = sort;
  }

  // Getter方法
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getTitle() {
    return title;
  }

  public String getComponent() {
    return component;
  }

  public String getPerms() {
    return perms;
  }

  public String getVisible() {
    return visible;
  }

  public String getIsCache() {
    return isCache;
  }

  public String getMenuType() {
    return menuType;
  }

  public Menu getParent() {
    return parent;
  }

  public Long getParentId() {
    return parent != null ? parent.getId() : null;
  }

  public String getPath() {
    return path;
  }

  public String getIcon() {
    return icon;
  }

  public Integer getSort() {
    return sort;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  // Setter方法
  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public void setPerms(String perms) {
    this.perms = perms;
  }

  public void setVisible(String visible) {
    this.visible = visible;
  }

  public void setIsCache(String isCache) {
    this.isCache = isCache;
  }

  public void setMenuType(String menuType) {
    this.menuType = menuType;
  }

  public void setParent(Menu parent) {
    this.parent = parent;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  // 业务方法
  @PreUpdate
  protected void onUpdate() {
    this.updateTime = LocalDateTime.now();
  }
}