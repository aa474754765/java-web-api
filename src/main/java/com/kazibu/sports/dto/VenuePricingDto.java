package com.kazibu.sports.dto;

import java.util.List;

public class VenuePricingDto {

  // 收费标准请求类
  public static class VenuePricingRequest {
    private Long id;
    private Long venueId; // 场馆ID
    private String dayType; // 日期类型：工作日、周末、节假日、全部
    private String timeSlotName; // 时间段名称，如"早场"、"午场"、"晚场"
    private String startTime; // 开始时间，格式：HH:mm，如 "06:00"
    private String endTime; // 结束时间，格式：HH:mm，如 "12:00"
    private Double price; // 价格
    private String unit; // 计价单位，如"小时"、"场次"、"天"
    private String description; // 备注说明
    private Integer order;
    private String enabled;

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

    public String getStartTime() {
      return startTime;
    }

    public void setStartTime(String startTime) {
      this.startTime = startTime;
    }

    public String getEndTime() {
      return endTime;
    }

    public void setEndTime(String endTime) {
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

  // 批量操作请求类
  public static class VenuePricingBatchRequest {
    private Long venueId; // 场馆ID
    private List<VenuePricingRequest> items; // 收费标准列表

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
    }

    public List<VenuePricingRequest> getItems() {
      return items;
    }

    public void setItems(List<VenuePricingRequest> items) {
      this.items = items;
    }
  }
}
