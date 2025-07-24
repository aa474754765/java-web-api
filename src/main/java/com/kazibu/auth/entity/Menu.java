package com.kazibu.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;

@Entity
@Table(name = "menu")
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Menu parent;

  @Column(length = 200)
  private String path;

  @Column(length = 100)
  private String icon;

  @Column(name = "sort")
  private Integer sort;

  @Column(name = "create_time", updatable = false)
  private LocalDateTime createTime = LocalDateTime.now();

  @Column(name = "update_time")
  private LocalDateTime updateTime = LocalDateTime.now();

  @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JsonIgnore
  private java.util.List<RoleMenu> roleMenus = new java.util.ArrayList<>();

  @Transient
  private java.util.List<Menu> children = new java.util.ArrayList<>();

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

  // Getter方法
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
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

  public java.util.List<Menu> getChildren() {
    return children;
  }

  public void setChildren(java.util.List<Menu> children) {
    this.children = children;
  }

  // Setter方法
  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
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