package com.kazibu.web_api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu")
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(length = 200)
  private String path;

  @Column(length = 100)
  private String icon;

  @Column(name = "sort")
  private Integer sort;

  @Column(name = "create_time")
  private LocalDateTime createTime = LocalDateTime.now();

  @Column(name = "update_time")
  private LocalDateTime updateTime = LocalDateTime.now();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getParentId() {
    return parentId;
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
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
}
