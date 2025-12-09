package com.kazibu.material.dto;

public class PhotoDto {

  // 上传图片请求类
  public static class PhotoUploadRequest {
    private String fileBase64; // Base64编码的文件内容
    private String fileName; // 文件名（包含扩展名）
    private String category; // 分类
    private String description; // 描述

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

    public String getCategory() {
      return category;
    }

    public void setCategory(String category) {
      this.category = category;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }

  // 图片请求类（用于增删改查）
  public static class PhotoRequest {
    private Long id;
    private String category;
    private String description;
    private String fileName;
    private String fileType;
    private String originalName;
    private Integer order;
    private String enabled;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getCategory() {
      return category;
    }

    public void setCategory(String category) {
      this.category = category;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
    }

    public String getFileType() {
      return fileType;
    }

    public void setFileType(String fileType) {
      this.fileType = fileType;
    }

    public String getOriginalName() {
      return originalName;
    }

    public void setOriginalName(String originalName) {
      this.originalName = originalName;
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
}
