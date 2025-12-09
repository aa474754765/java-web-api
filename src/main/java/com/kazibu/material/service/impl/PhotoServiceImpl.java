package com.kazibu.material.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import com.kazibu.material.entity.Photo;
import com.kazibu.material.repository.PhotoRepository;
import com.kazibu.material.service.PhotoService;

@Service
public class PhotoServiceImpl implements PhotoService {

  @Autowired
  private PhotoRepository repository;

  @Value("${photo.upload.path:uploads}")
  private String uploadPath;

  @Value("${photo.upload.url-prefix:/uploads}")
  private String urlPrefix;

  @Override
  @Transactional
  public Photo uploadBase64(String fileBase64, String fileName, String category, String description) throws Exception {
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

    // 创建上传目录（如果不存在）
    Path uploadDir = Paths.get(uploadPath);
    if (!Files.exists(uploadDir)) {
      Files.createDirectories(uploadDir);
    }

    // 保存文件
    Path filePath = uploadDir.resolve(uniqueFileName);
    Files.write(filePath, fileBytes);

    // 创建Photo实体
    Photo photo = new Photo();
    photo.setFileName(uniqueFileName);
    photo.setOriginalName(fileName);
    photo.setFilePath(filePath.toString());
    photo.setFileUrl(urlPrefix + "/" + uniqueFileName);
    photo.setFileType(fileExtension.toLowerCase());
    photo.setFileSize((long) fileBytes.length);
    photo.setMimeType(mimeType);
    photo.setCategory(category);
    photo.setDescription(description);

    return repository.save(photo);
  }

  @Override
  @Transactional
  public Photo update(Photo photo) {
    return repository.save(photo);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    Photo photo = repository.findById(id).orElse(null);
    if (photo != null) {
      // 删除文件
      try {
        Path filePath = Paths.get(photo.getFilePath());
        if (Files.exists(filePath)) {
          Files.delete(filePath);
        }
      } catch (IOException e) {
        // 记录日志，但不阻止删除数据库记录
        System.err.println("删除文件失败: " + photo.getFilePath() + ", " + e.getMessage());
      }
      // 删除数据库记录
      repository.deleteById(id);
    }
  }

  @Override
  public Photo get(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<Photo> list(String category, String fileName, String fileType, String originalName) {
    // 使用 Specification 动态构建查询条件
    Specification<Photo> spec = (root, query, cb) -> {
      List<Predicate> predicates = new java.util.ArrayList<>();

      // 如果参数不为空，添加对应的查询条件
      if (category != null && !category.trim().isEmpty()) {
        predicates.add(cb.equal(root.get("category"), category.trim()));
      }
      if (fileName != null && !fileName.trim().isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("fileName")), "%" + fileName.trim().toLowerCase() + "%"));
      }
      if (fileType != null && !fileType.trim().isEmpty()) {
        predicates.add(cb.equal(root.get("fileType"), fileType.trim()));
      }
      if (originalName != null && !originalName.trim().isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("originalName")), "%" + originalName.trim().toLowerCase() + "%"));
      }

      // 按 order 字段升序排序
      if (query != null) {
        query.orderBy(cb.asc(root.get("order")));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    return repository.findAll(spec);
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
