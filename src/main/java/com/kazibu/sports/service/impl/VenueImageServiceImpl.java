package com.kazibu.sports.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kazibu.sports.entity.VenueImage;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.repository.VenueImageRepository;
import com.kazibu.sports.repository.VenueRepository;
import com.kazibu.sports.service.VenueImageService;

@Service
public class VenueImageServiceImpl implements VenueImageService {

  @Autowired
  private VenueImageRepository repository;

  @Autowired
  private VenueRepository venueRepository;

  @Value("${photo.upload.path:uploads}")
  private String uploadPath;

  @Value("${photo.upload.url-prefix:/uploads}")
  private String urlPrefix;

  @Override
  @Transactional
  public VenueImage uploadBase64(Long venueId, String fileBase64, String fileName, String description)
      throws Exception {
    // 验证场馆是否存在
    Venue venue = venueRepository.findById(venueId)
        .orElseThrow(() -> new IllegalArgumentException("场馆不存在，ID: " + venueId));

    if (fileBase64 == null || fileBase64.trim().isEmpty()) {
      throw new IllegalArgumentException("文件内容不能为空");
    }

    if (fileName == null || fileName.trim().isEmpty()) {
      throw new IllegalArgumentException("文件名不能为空");
    }

    // 处理Base64字符串（可能包含data:image/png;base64,前缀）
    String base64Data = fileBase64;
    String mimeType = "image/jpeg"; // 默认MIME类型
    if (fileBase64.contains(",")) {
      String[] parts = fileBase64.split(",", 2);
      if (parts.length == 2) {
        String header = parts[0];
        base64Data = parts[1];
        if (header.contains("data:")) {
          String mimePart = header.substring(header.indexOf("data:") + 5);
          if (mimePart.contains(";")) {
            mimeType = mimePart.substring(0, mimePart.indexOf(";"));
          } else {
            mimeType = mimePart;
          }
        }
      }
    }

    // 验证文件类型（仅允许图片）
    String fileExtension = getFileExtension(fileName);
    if (!isImageFile(fileExtension)) {
      throw new IllegalArgumentException("仅支持图片文件格式：jpg, jpeg, png, gif, webp, bmp");
    }

    // 解码Base64
    byte[] fileBytes;
    try {
      fileBytes = Base64.getDecoder().decode(base64Data);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Base64编码格式错误");
    }

    // 生成唯一文件名
    String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

    // 创建上传目录（如果不存在）- 使用uploads/venue子目录
    Path venueDir = Paths.get(uploadPath, "venue");
    if (!Files.exists(venueDir)) {
      Files.createDirectories(venueDir);
    }

    // 保存文件
    Path filePath = venueDir.resolve(uniqueFileName);
    Files.write(filePath, fileBytes);

    // 创建VenueImage实体
    VenueImage venueImage = new VenueImage();
    venueImage.setVenue(venue);
    venueImage.setFileName(uniqueFileName);
    venueImage.setOriginalName(fileName);
    venueImage.setFilePath(filePath.toString());
    venueImage.setFileUrl(urlPrefix + "/venue/" + uniqueFileName);
    venueImage.setFileType(fileExtension.toLowerCase());
    venueImage.setFileSize((long) fileBytes.length);
    venueImage.setMimeType(mimeType);
    venueImage.setDescription(description);

    return repository.save(venueImage);
  }

  @Override
  @Transactional
  public List<VenueImage> uploadBatch(Long venueId, List<VenueImageUploadItem> items) throws Exception {
    // 验证场馆是否存在（提前验证，避免在循环中多次验证）
    venueRepository.findById(venueId)
        .orElseThrow(() -> new IllegalArgumentException("场馆不存在，ID: " + venueId));

    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("上传列表不能为空");
    }

    List<VenueImage> venueImages = new java.util.ArrayList<>();
    for (VenueImageUploadItem item : items) {
      VenueImage image = uploadBase64(venueId, item.getFileBase64(), item.getFileName(), item.getDescription());
      if (item.getOrder() != null) {
        image.setOrder(item.getOrder());
        image = repository.save(image);
      }
      venueImages.add(image);
    }

    return venueImages;
  }

  @Override
  @Transactional
  public VenueImage update(VenueImage venueImage) {
    return repository.save(venueImage);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    VenueImage venueImage = repository.findById(id).orElse(null);
    if (venueImage != null) {
      // 删除文件
      try {
        Path filePath = Paths.get(venueImage.getFilePath());
        if (Files.exists(filePath)) {
          Files.delete(filePath);
        }
      } catch (IOException e) {
        // 记录日志，但不阻止删除数据库记录
        System.err.println("删除文件失败: " + venueImage.getFilePath() + ", " + e.getMessage());
      }
      // 删除数据库记录
      repository.deleteById(id);
    }
  }

  @Override
  public List<VenueImage> listByVenueId(Long venueId) {
    return repository.findByVenueIdOrderByOrderAsc(venueId);
  }

  private String getFileExtension(String filename) {
    int lastDotIndex = filename.lastIndexOf('.');
    if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
      return filename.substring(lastDotIndex + 1).toLowerCase();
    }
    return "";
  }

  private boolean isImageFile(String extension) {
    String[] imageExtensions = { "jpg", "jpeg", "png", "gif", "webp", "bmp" };
    for (String ext : imageExtensions) {
      if (ext.equalsIgnoreCase(extension)) {
        return true;
      }
    }
    return false;
  }
}
