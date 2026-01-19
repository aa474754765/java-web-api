package com.kazibu.sports.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Table(name = "venue_pricing")
public class VenuePricing {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "venue_id", nullable = false)
  private Venue venue;

  @Column(name = "day_type", length = 50)
  private String dayType; // 工作日、周末、节假日、全部

  @Column(name = "time_slot_name", length = 100)
  private String timeSlotName; // 时间段名称，如"早场"、"午场"、"晚场"

  @Column(name = "start_time")
  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime; // 开始时间，如 06:00

  @Column(name = "end_time")
  @JsonFormat(pattern = "HH:mm")
  private LocalTime endTime; // 结束时间，如 12:00

  @Column(name = "price", nullable = false)
  private Double price; // 价格

  @Column(name = "unit", length = 50)
  private String unit; // 计价单位，如"小时"、"场次"、"天"

  @Column(name = "description", length = 500)
  private String description; // 备注说明

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

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createdTime = now;
    this.updateTime = now;
    String username = resolveCurrentUsername();
    this.createdBy = username;
    this.updateBy = username;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateTime = LocalDateTime.now();
    this.updateBy = resolveCurrentUsername();
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

  public Venue getVenue() {
    return venue;
  }

  public void setVenue(Venue venue) {
    this.venue = venue;
  }

  public String getDayType() {
    return dayType;
  }

  public void setDayType(String dayType) {
    this.dayType = dayType;
  }

  public String getTimeSlotName() {
    return timeSlotName;
  }

  public void setTimeSlotName(String timeSlotName) {
    this.timeSlotName = timeSlotName;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
}
