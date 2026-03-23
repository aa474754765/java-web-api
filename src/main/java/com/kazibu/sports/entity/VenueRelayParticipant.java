package com.kazibu.sports.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "venue_relay_participant",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_relay_user", columnNames = { "relay_id", "user_id" })
    })
public class VenueRelayParticipant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "relay_id", nullable = false)
  private VenueRelay relay;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "user_name", nullable = false, length = 100)
  private String userName;

  @Column(name = "contact_info", length = 255)
  private String contactInfo;

  /**
   * 1-已参与，0-已取消
   */
  @Column(name = "status", nullable = false, length = 1)
  private String status = "1";

  @Column(name = "join_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime joinTime;

  @Column(name = "cancel_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime cancelTime;

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
    if (this.joinTime == null) {
      this.joinTime = now;
    }
    if (this.status == null || this.status.trim().isEmpty()) {
      this.status = "1";
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updateTime = LocalDateTime.now();
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

  public VenueRelay getRelay() {
    return relay;
  }

  public void setRelay(VenueRelay relay) {
    this.relay = relay;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  public LocalDateTime getJoinTime() {
    return joinTime;
  }

  public void setJoinTime(LocalDateTime joinTime) {
    this.joinTime = joinTime;
  }

  public LocalDateTime getCancelTime() {
    return cancelTime;
  }

  public void setCancelTime(LocalDateTime cancelTime) {
    this.cancelTime = cancelTime;
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
