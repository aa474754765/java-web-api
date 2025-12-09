package com.kazibu.material.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kazibu.material.entity.Photo;
import com.kazibu.material.dto.PhotoDto;
import com.kazibu.material.service.PhotoService;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.security.RequiresPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/material_photo")
@Tag(name = "图片管理", description = "图片上传、查询、删除接口")
public class PhotoController {

  @Autowired
  private PhotoService service;

  @PostMapping("/upload")
  @RequiresPermission("material:photo:upload")
  @Operation(summary = "上传图片", description = "上传图片文件到服务器（Base64编码）")
  public Result<Photo> upload(@RequestBody PhotoDto.PhotoUploadRequest req) {
    try {
      Photo photo = service.uploadBase64(req.getFileBase64(), req.getFileName(), req.getCategory(),
          req.getDescription());
      return Result.success(photo);
    } catch (IllegalArgumentException e) {
      return Result.error("UPLOAD_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("UPLOAD_ERROR", "上传失败: " + e.getMessage());
    }
  }

  @PostMapping("/edit")
  @RequiresPermission("material:photo:edit")
  @Operation(summary = "编辑图片信息", description = "更新图片的分类、描述等信息")
  public Result<Photo> edit(@RequestBody PhotoDto.PhotoRequest req) {
    Photo photo = service.get(req.getId());
    if (photo == null) {
      return Result.error("NOT_FOUND", "图片不存在");
    }
    if (req.getCategory() != null) {
      photo.setCategory(req.getCategory());
    }
    if (req.getDescription() != null) {
      photo.setDescription(req.getDescription());
    }
    if (req.getOrder() != null) {
      photo.setOrder(req.getOrder());
    }
    if (req.getEnabled() != null) {
      photo.setEnabled(req.getEnabled());
    }
    return Result.success(service.update(photo));
  }

  @PostMapping("/delete")
  @RequiresPermission("material:photo:delete")
  @Operation(summary = "删除图片", description = "删除图片记录和文件")
  public Result<String> delete(@RequestBody PhotoDto.PhotoRequest req) {
    service.delete(req.getId());
    return Result.success("删除成功");
  }

  @PostMapping("/get")
  @RequiresPermission("material:photo:get")
  @Operation(summary = "获取图片详情", description = "根据ID获取图片详细信息")
  public Result<Photo> get(@RequestBody PhotoDto.PhotoRequest req) {
    return Result.success(service.get(req.getId()));
  }

  @PostMapping("/list")
  @RequiresPermission("material:photo:list")
  @Operation(summary = "获取图片列表", description = "获取图片列表，支持按分类、文件名、文件类型、原始文件名筛选。参数为空时不使用该条件，可组合多个条件查询")
  public Result<List<Photo>> list(@RequestBody(required = false) PhotoDto.PhotoRequest req) {
    String category = req == null ? null : req.getCategory();
    String fileName = req == null ? null : req.getFileName();
    String fileType = req == null ? null : req.getFileType();
    String originalName = req == null ? null : req.getOriginalName();
    return Result.success(service.list(category, fileName, fileType, originalName));
  }
}
