package com.kazibu.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @Column(length = 200)
  private String description;

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

  public String getDescription() {
    return description;
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

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
