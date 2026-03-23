package com.kazibu.sports.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "venue_relay")
public class VenueRelay {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "venue_id", nullable = false)
  private Venue venue;

  @Column(name = "start_date", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @Column(name = "start_time", nullable = false)
  @JsonFormat(pattern = "HH:mm:ss")
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  @JsonFormat(pattern = "HH:mm:ss")
  private LocalTime endTime;

  @Column(name = "court_name", nullable = false, length = 255)
  private String courtName;

  @Column(name = "max_people", nullable = false)
  private Integer maxPeople;

  @Column(name = "joined_people", nullable = false)
  private Integer joinedPeople = 0;

  @Column(name = "contact_info", length = 255)
  private String contactInfo;

  /**
   * 1-进行中，2-已结束，3-已取消
   */
  @Column(name = "status", nullable = false, length = 1)
  private String status = "1";

  @Column(name = "avg_cost", precision = 10, scale = 2)
  private BigDecimal avgCost;

  @Column(name = "remark", length = 1000)
  private String remark;

  @Column(name = "creator_user_id", nullable = false)
  private Long creatorUserId;

  @Column(name = "creator_username", nullable = false, length = 100)
  private String creatorUsername;

  @Column(name = "create_time", updatable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  @Column(name = "update_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updateTime;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createTime = now;
    this.updateTime = now;
    if (this.joinedPeople == null) {
      this.joinedPeople = 0;
    }
    if (this.status == null || this.status.trim().isEmpty()) {
      this.status = "1";
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateTime = LocalDateTime.now();
    if (this.joinedPeople == null) {
      this.joinedPeople = 0;
    }
    if (this.status == null || this.status.trim().isEmpty()) {
      this.status = "1";
    }
  }

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

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
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

  public String getCourtName() {
    return courtName;
  }

  public void setCourtName(String courtName) {
    this.courtName = courtName;
  }

  public Integer getMaxPeople() {
    return maxPeople;
  }

  public void setMaxPeople(Integer maxPeople) {
    this.maxPeople = maxPeople;
  }

  public Integer getJoinedPeople() {
    return joinedPeople;
  }

  public void setJoinedPeople(Integer joinedPeople) {
    this.joinedPeople = joinedPeople;
  }

  public String getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(String contactInfo) {
    this.contactInfo = contactInfo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getAvgCost() {
    return avgCost;
  }

  public void setAvgCost(BigDecimal avgCost) {
    this.avgCost = avgCost;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Long getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Long creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public String getCreatorUsername() {
    return creatorUsername;
  }

  public void setCreatorUsername(String creatorUsername) {
    this.creatorUsername = creatorUsername;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
