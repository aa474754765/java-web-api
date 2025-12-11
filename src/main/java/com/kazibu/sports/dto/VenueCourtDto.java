package com.kazibu.sports.dto;

import java.util.List;

public class VenueCourtDto {

  // 场地请求类
  public static class VenueCourtRequest {
    private Long id;
    private String name; // 场地名称
    private String type; // 场地类型：室内场、室外场等
    private String description; // 描述
    private Integer order;
    private String enabled;

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

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
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
  public static class VenueCourtBatchRequest {
    private Long venueId; // 场馆ID
    private List<VenueCourtRequest> items; // 场地列表

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
    }

    public List<VenueCourtRequest> getItems() {
      return items;
    }

    public void setItems(List<VenueCourtRequest> items) {
      this.items = items;
    }
  }
}

