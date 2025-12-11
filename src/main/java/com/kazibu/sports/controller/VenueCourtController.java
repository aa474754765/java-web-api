package com.kazibu.sports.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kazibu.sports.entity.VenueCourt;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.dto.VenueCourtDto;
import com.kazibu.sports.service.VenueCourtService;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.security.RequiresPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/sports_venue_court")
@Tag(name = "场地管理", description = "场地的增删改查接口")
public class VenueCourtController {

  @Autowired
  private VenueCourtService service;

  @PostMapping("/edit")
  @RequiresPermission("sports:venue:edit")
  @Operation(summary = "编辑场地", description = "替换场馆的所有场地（先删除该场馆的所有现有场地，再保存新的列表）。如果items为空数组，则删除该场馆的所有场地")
  public Result<List<VenueCourt>> edit(@RequestBody VenueCourtDto.VenueCourtBatchRequest req) {
    try {
      if (req == null || req.getVenueId() == null) {
        return Result.error("VALIDATION_ERROR", "场馆ID不能为空");
      }

      // items可以为空（表示删除所有场地）
      List<VenueCourt> courts = new java.util.ArrayList<>();
      if (req.getItems() != null && !req.getItems().isEmpty()) {
        courts = req.getItems().stream()
            .map(item -> {
              VenueCourt court = convertToEntity(item);
              // 确保所有场地都关联到同一个场馆
              if (court.getVenue() == null || court.getVenue().getId() == null) {
                Venue venue = new Venue();
                venue.setId(req.getVenueId());
                court.setVenue(venue);
              }
              return court;
            })
            .toList();
      }

      return Result.success(service.replaceByVenueId(req.getVenueId(), courts));
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("UPDATE_ERROR", "更新失败: " + e.getMessage());
    }
  }

  // DTO转Entity的辅助方法
  private VenueCourt convertToEntity(VenueCourtDto.VenueCourtRequest req) {
    VenueCourt court = new VenueCourt();
    if (req.getId() != null) {
      court.setId(req.getId());
    }
    if (req.getName() != null) {
      court.setName(req.getName());
    }
    if (req.getType() != null) {
      court.setType(req.getType());
    }
    if (req.getDescription() != null) {
      court.setDescription(req.getDescription());
    }
    if (req.getOrder() != null) {
      court.setOrder(req.getOrder());
    }
    if (req.getEnabled() != null) {
      court.setEnabled(req.getEnabled());
    }
    return court;
  }
}

