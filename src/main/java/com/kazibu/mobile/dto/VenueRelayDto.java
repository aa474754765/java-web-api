package com.kazibu.mobile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class VenueRelayDto {
  public static class CreateRequest {
    private Long venueId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Integer maxPeople;
    private String contactInfo;
    private BigDecimal avgCost;
    private String isPublic; // 是否公开：1-公开，0-不公开
    private String remark;
    private String venueImage; // 场馆图片
    private String skillLevel; // 水平等级

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
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

    public Integer getMaxPeople() {
      return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
      this.maxPeople = maxPeople;
    }

    public String getContactInfo() {
      return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
      this.contactInfo = contactInfo;
    }

    public BigDecimal getAvgCost() {
      return avgCost;
    }

    public void setAvgCost(BigDecimal avgCost) {
      this.avgCost = avgCost;
    }

    public String getIsPublic() {
      return isPublic;
    }

    public void setIsPublic(String isPublic) {
      this.isPublic = isPublic;
    }

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
    }

    public String getVenueImage() {
      return venueImage;
    }

    public void setVenueImage(String venueImage) {
      this.venueImage = venueImage;
    }

    public String getSkillLevel() {
      return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
      this.skillLevel = skillLevel;
    }
  }

  /** 编辑接龙：字段同创建请求，另需 relayId */
  public static class EditRequest extends CreateRequest {
    private Long relayId;

    public Long getRelayId() {
      return relayId;
    }

    public void setRelayId(Long relayId) {
      this.relayId = relayId;
    }
  }

  public static class JoinRequest {
    private Long relayId;
    private String contactInfo;

    public Long getRelayId() {
      return relayId;
    }

    public void setRelayId(Long relayId) {
      this.relayId = relayId;
    }

    public String getContactInfo() {
      return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
      this.contactInfo = contactInfo;
    }
  }

  public static class CancelRequest {
    private Long relayId;

    public Long getRelayId() {
      return relayId;
    }

    public void setRelayId(Long relayId) {
      this.relayId = relayId;
    }
  }

  public static class RelayPageRequest {
    private Integer page = 0;
    private Integer size = 10;
    private Long venueId;
    private Integer tabType; // 1-我发起的，2-我参与的(不含我发起)，3-全部
    private Boolean selfOnly; // 是否仅查询本人创建的接龙
    private String isPublic; // 是否公开筛选：1-公开，0-不公开
    private String skillLevel; // 水平等级筛选
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    public Integer getPage() {
      return page;
    }

    public void setPage(Integer page) {
      this.page = page;
    }

    public Integer getSize() {
      return size;
    }

    public void setSize(Integer size) {
      this.size = size;
    }

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
    }

    public Integer getTabType() {
      return tabType;
    }

    public void setTabType(Integer tabType) {
      this.tabType = tabType;
    }

    public Boolean getSelfOnly() {
      return selfOnly;
    }

    public void setSelfOnly(Boolean selfOnly) {
      this.selfOnly = selfOnly;
    }

    public String getIsPublic() {
      return isPublic;
    }

    public void setIsPublic(String isPublic) {
      this.isPublic = isPublic;
    }

    public String getSkillLevel() {
      return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
      this.skillLevel = skillLevel;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public LocalDate getStartDate() {
      return startDate;
    }

    public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
    }
  }

  /** 列表中返回的场馆简要信息 */
  public static class VenueInfo {
    private Long id;
    private String name;
    private String description;
    private Long sportsTypeId;
    private String sportsTypeName;
    private String province;
    private String city;
    private String district;
    private String address;
    private Double latitude;
    private Double longitude;
    private String contactType;
    private String contactInfo;
    private Double rating;

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

    public Long getSportsTypeId() {
      return sportsTypeId;
    }

    public void setSportsTypeId(Long sportsTypeId) {
      this.sportsTypeId = sportsTypeId;
    }

    public String getSportsTypeName() {
      return sportsTypeName;
    }

    public void setSportsTypeName(String sportsTypeName) {
      this.sportsTypeName = sportsTypeName;
    }

    public String getProvince() {
      return province;
    }

    public void setProvince(String province) {
      this.province = province;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getDistrict() {
      return district;
    }

    public void setDistrict(String district) {
      this.district = district;
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
  }

  public static class RelayListItem {
    public static class ParticipantInfo {
      private String userName;
      private String userAvatar;

      public String getUserName() {
        return userName;
      }

      public void setUserName(String userName) {
        this.userName = userName;
      }

      public String getUserAvatar() {
        return userAvatar;
      }

      public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
      }
    }

    private Long id;
    private VenueInfo venueInfo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Integer maxPeople;
    private Integer joinedPeople;
    private String contactInfo;
    /**
     * 接龙状态
     * 1 - 进行中
     * 2 - 已结束
     * 3 - 已取消
     */
    private String status;
    private String isPublic;
    private BigDecimal avgCost;
    private String remark;
    private String venueImage;
    private String skillLevel;
    private Long creatorUserId;
    private String creatorUsername;
    /**
     * joinedByCurrentUser 字段表示当前用户是否已加入该接龙（true 已加入，false 未加入）。
     * participants 字段表示已加入该接龙的参与人信息数组（用户名、头像）。
     * createTime 字段表示接龙创建的日期和时间（格式为 yyyy-MM-dd HH:mm:ss）。
     */
    private Boolean joinedByCurrentUser;
    private List<ParticipantInfo> participants;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public VenueInfo getVenueInfo() {
      return venueInfo;
    }

    public void setVenueInfo(VenueInfo venueInfo) {
      this.venueInfo = venueInfo;
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

    public String getIsPublic() {
      return isPublic;
    }

    public void setIsPublic(String isPublic) {
      this.isPublic = isPublic;
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

    public String getVenueImage() {
      return venueImage;
    }

    public void setVenueImage(String venueImage) {
      this.venueImage = venueImage;
    }

    public String getSkillLevel() {
      return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
      this.skillLevel = skillLevel;
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

    public Boolean getJoinedByCurrentUser() {
      return joinedByCurrentUser;
    }

    public void setJoinedByCurrentUser(Boolean joinedByCurrentUser) {
      this.joinedByCurrentUser = joinedByCurrentUser;
    }

    public List<ParticipantInfo> getParticipants() {
      return participants;
    }

    public void setParticipants(List<ParticipantInfo> participants) {
      this.participants = participants;
    }

    public LocalDateTime getCreateTime() {
      return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
    }
  }

  public static class PageResponse<T> {
    private List<T> list;
    private Integer page;
    private Integer size;
    private Long total;
    private Integer totalPages;

    public List<T> getList() {
      return list;
    }

    public void setList(List<T> list) {
      this.list = list;
    }

    public Integer getPage() {
      return page;
    }

    public void setPage(Integer page) {
      this.page = page;
    }

    public Integer getSize() {
      return size;
    }

    public void setSize(Integer size) {
      this.size = size;
    }

    public Long getTotal() {
      return total;
    }

    public void setTotal(Long total) {
      this.total = total;
    }

    public Integer getTotalPages() {
      return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
      this.totalPages = totalPages;
    }
  }
}
