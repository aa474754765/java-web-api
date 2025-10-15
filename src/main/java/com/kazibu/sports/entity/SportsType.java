package com.kazibu.sports.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Table(name = "sports_type")
public class SportsType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "type", nullable = false, length = 100)
  private String type;

  @Column(name = "created_by", updatable = false, length = 100)
  private String createdBy;

  @Column(name = "created_time", updatable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdTime;

  @Column(name = "update_by", length = 100)
  private String updateBy;

  @Column(name = "update_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

  @Column(name = "sort_order")
  private Integer order = 1;

  @Column(name = "enabled", length = 1)
  private String enabled = "1";

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createdTime = now;
    this.updateTime = now;
    String username = resolveCurrentUsername();
    this.createdBy = username;
    this.updateBy = username;
    if (this.order == null) {
      this.order = 1;
    }
    if (this.enabled == null || this.enabled.trim().isEmpty()) {
      this.enabled = "1";
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateTime = LocalDateTime.now();
    this.updateBy = resolveCurrentUsername();
    if (this.order == null) {
      this.order = 1;
    }
    if (this.enabled == null || this.enabled.trim().isEmpty()) {
      this.enabled = "1";
    }
  }

  private String resolveCurrentUsername() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated()) {
        return authentication.getName();
      }
    } catch (Exception ignored) {
    }
    return "system";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(LocalDateTime createdTime) {
    this.createdTime = createdTime;
  }

  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getEnabled() {
    return enabled;
  }

  public void setEnabled(String enabled) {
    this.enabled = enabled;
  }
}
