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
    private String courtName;
    private Integer maxPeople;
    private String contactInfo;
    private BigDecimal avgCost;
    private String remark;

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

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
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

  public static class RelayListItem {
    private Long id;
    private Long venueId;
    private String venueName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private String courtName;
    private Integer maxPeople;
    private Integer joinedPeople;
    private String contactInfo;
    private String status;
    private BigDecimal avgCost;
    private String remark;
    private Long creatorUserId;
    private String creatorUsername;
    private Boolean joinedByCurrentUser;
    private List<String> participantUserNames;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
    }

    public String getVenueName() {
      return venueName;
    }

    public void setVenueName(String venueName) {
      this.venueName = venueName;
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

    public Boolean getJoinedByCurrentUser() {
      return joinedByCurrentUser;
    }

    public void setJoinedByCurrentUser(Boolean joinedByCurrentUser) {
      this.joinedByCurrentUser = joinedByCurrentUser;
    }

    public List<String> getParticipantUserNames() {
      return participantUserNames;
    }

    public void setParticipantUserNames(List<String> participantUserNames) {
      this.participantUserNames = participantUserNames;
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
