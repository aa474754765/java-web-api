package com.kazibu.sports.service;

import java.util.List;
import com.kazibu.sports.entity.VenueImage;

public interface VenueImageService {
  VenueImage uploadBase64(Long venueId, String fileBase64, String fileName, String description) throws Exception;

  List<VenueImage> uploadBatch(Long venueId, List<VenueImageUploadItem> items) throws Exception;

  VenueImage update(VenueImage venueImage);

  void delete(Long id);

  List<VenueImage> listByVenueId(Long venueId);

  // 内部类用于批量上传
  class VenueImageUploadItem {
    private String fileBase64;
    private String fileName;
    private String description;
    private Integer order;

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
}

