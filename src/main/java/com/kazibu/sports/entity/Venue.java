package com.kazibu.sports.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "venue")
public class Venue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "description", length = 1000)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sports_type_id", nullable = false)
  private SportsType sportsType;

  @Column(name = "city", length = 100)
  private String city;

  @Column(name = "address", length = 500)
  private String address;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  @Column(name = "contact_type", length = 50)
  private String contactType; // 电话、小程序、微信等

  @Column(name = "contact_info", length = 255)
  private String contactInfo;

  @Column(name = "rating")
  private Double rating = 5.0; // 1-5分，默认5分

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
    if (this.rating == null) {
      this.rating = 5.0;
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
    if (this.rating == null) {
      this.rating = 5.0;
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

  // Getters and Setters
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

  public SportsType getSportsType() {
    return sportsType;
  }

  public void setSportsType(SportsType sportsType) {
    this.sportsType = sportsType;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public String getContactType() {
    return contactType;
  }

  public void setContactType(String contactType) {
    this.contactType = contactType;
  }

  public String getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(String contactInfo) {
    this.contactInfo = contactInfo;
  }

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
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
