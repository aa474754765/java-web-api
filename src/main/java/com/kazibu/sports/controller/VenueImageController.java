package com.kazibu.sports.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kazibu.sports.entity.VenueImage;
import com.kazibu.sports.dto.VenueImageDto;
import com.kazibu.sports.service.VenueImageService;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.security.RequiresPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/sports_venue_image")
@Tag(name = "场馆图片管理", description = "场馆图片的上传、删除、查询接口")
public class VenueImageController {

  @Autowired
  private VenueImageService service;

  @PostMapping("/upload")
  @RequiresPermission("sports:venue:image")
  @Operation(summary = "上传单张图片", description = "为指定场馆上传单张图片（Base64编码）")
  public Result<VenueImage> upload(@RequestBody VenueImageDto.VenueImageUploadRequest req) {
    try {
      if (req == null || req.getVenueId() == null) {
        return Result.error("VALIDATION_ERROR", "场馆ID不能为空");
      }
      VenueImage image = service.uploadBase64(req.getVenueId(), req.getFileBase64(), req.getFileName(),
          req.getDescription());
      if (req.getOrder() != null) {
        image.setOrder(req.getOrder());
        image = service.update(image);
      }
      return Result.success(image);
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("UPLOAD_ERROR", "上传失败: " + e.getMessage());
    }
  }

  @PostMapping("/uploadBatch")
  @RequiresPermission("sports:venue:image")
  @Operation(summary = "批量上传图片", description = "为指定场馆批量上传多张图片（Base64编码）")
  public Result<List<VenueImage>> uploadBatch(@RequestBody VenueImageDto.VenueImageBatchUploadRequest req) {
    try {
      if (req == null || req.getVenueId() == null) {
        return Result.error("VALIDATION_ERROR", "场馆ID不能为空");
      }
      if (req.getItems() == null || req.getItems().isEmpty()) {
        return Result.error("VALIDATION_ERROR", "上传列表不能为空");
      }
      // 转换为Service层的UploadItem
      List<VenueImageService.VenueImageUploadItem> items = req.getItems().stream()
          .map(item -> {
            VenueImageService.VenueImageUploadItem uploadItem = new VenueImageService.VenueImageUploadItem();
            uploadItem.setFileBase64(item.getFileBase64());
            uploadItem.setFileName(item.getFileName());
            uploadItem.setDescription(item.getDescription());
            uploadItem.setOrder(item.getOrder());
            return uploadItem;
          })
          .toList();
      List<VenueImage> images = service.uploadBatch(req.getVenueId(), items);
      return Result.success(images);
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("UPLOAD_ERROR", "上传失败: " + e.getMessage());
    }
  }

  @PostMapping("/delete")
  @RequiresPermission("sports:venue:image:delete")
  @Operation(summary = "删除图片", description = "删除指定图片（同时删除文件）")
  public Result<String> delete(@RequestBody VenueImageDto.VenueImageDeleteRequest req) {
    try {
      if (req == null || req.getId() == null) {
        return Result.error("VALIDATION_ERROR", "图片ID不能为空");
      }
      service.delete(req.getId());
      return Result.success("删除成功");
    } catch (Exception e) {
      return Result.error("DELETE_ERROR", "删除失败: " + e.getMessage());
    }
  }

  @PostMapping("/list")
  @RequiresPermission("sports:venue:image:list")
  @Operation(summary = "查询图片列表", description = "根据场馆ID查询该场馆的所有图片列表")
  public Result<List<VenueImage>> list(@RequestBody VenueImageDto.VenueImageListRequest req) {
    try {
      if (req == null || req.getVenueId() == null) {
        return Result.error("VALIDATION_ERROR", "场馆ID不能为空");
      }
      List<VenueImage> images = service.listByVenueId(req.getVenueId());
      return Result.success(images);
    } catch (Exception e) {
      return Result.error("QUERY_ERROR", "查询失败: " + e.getMessage());
    }
  }
}
