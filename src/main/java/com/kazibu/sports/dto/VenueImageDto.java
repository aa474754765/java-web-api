package com.kazibu.sports.dto;

import java.util.List;

public class VenueImageDto {

  // 单张图片上传请求类
  public static class VenueImageUploadRequest {
    private Long venueId; // 场馆ID
    private String fileBase64; // Base64编码的文件内容
    private String fileName; // 文件名（包含扩展名）
    private String description; // 描述
    private Integer order; // 排序

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
    }

    public String getFileBase64() {
      return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
      this.fileBase64 = fileBase64;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
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
  }

  // 批量上传请求类
  public static class VenueImageBatchUploadRequest {
    private Long venueId; // 场馆ID
    private List<VenueImageUploadItem> items; // 图片列表

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
    }

    public List<VenueImageUploadItem> getItems() {
      return items;
    }

    public void setItems(List<VenueImageUploadItem> items) {
      this.items = items;
    }
  }

  // 批量上传项
  public static class VenueImageUploadItem {
    private String fileBase64; // Base64编码的文件内容
    private String fileName; // 文件名（包含扩展名）
    private String description; // 描述
    private Integer order; // 排序

    public String getFileBase64() {
      return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
      this.fileBase64 = fileBase64;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
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
  }

  // 删除请求类
  public static class VenueImageDeleteRequest {
    private Long id; // 图片ID

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }

  // 查询请求类
  public static class VenueImageListRequest {
    private Long venueId; // 场馆ID

    public Long getVenueId() {
      return venueId;
    }

    public void setVenueId(Long venueId) {
      this.venueId = venueId;
    }
  }
}

