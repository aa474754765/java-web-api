package com.kazibu.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

  @Column(nullable = false)
  private Boolean status = true;

  @Column(name = "role_name", nullable = false, length = 100)
  private String roleName;

  @Column(name = "create_time")
  private LocalDateTime createTime = LocalDateTime.now();

  @Column(name = "update_time")
  private LocalDateTime updateTime = LocalDateTime.now();

  @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JsonIgnore
  private java.util.List<UserRole> userRoles = new java.util.ArrayList<>();

  @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JsonIgnore
  private java.util.List<RoleMenu> roleMenus = new java.util.ArrayList<>();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getStatus() {
    return status;
  }

  public String getRoleName() {
    return roleName;
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

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
