package com.kazibu.sports.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kazibu.sports.entity.VenuePricing;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.dto.VenuePricingDto;
import com.kazibu.sports.service.VenuePricingService;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.security.RequiresPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/sports_venue_pricing")
@Tag(name = "场馆收费标准管理", description = "场馆营业时间和收费标准的增删改查接口")
public class VenuePricingController {

  @Autowired
  private VenuePricingService service;

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  @PostMapping("/edit")
  @RequiresPermission("sports:venue:price")
  @Operation(summary = "编辑收费标准", description = "替换场馆的所有收费标准（先删除该场馆的所有现有收费标准，再保存新的列表）。如果items为空数组，则删除该场馆的所有收费标准")
  public Result<List<VenuePricing>> edit(@RequestBody VenuePricingDto.VenuePricingBatchRequest req) {
    try {
      if (req == null || req.getVenueId() == null) {
        return Result.error("VALIDATION_ERROR", "场馆ID不能为空");
      }

      // items可以为空（表示删除所有收费标准）
      List<VenuePricing> venuePricings = new java.util.ArrayList<>();
      if (req.getItems() != null && !req.getItems().isEmpty()) {
        venuePricings = req.getItems().stream()
            .map(item -> {
              VenuePricing pricing = convertToEntity(item);
              // 确保所有收费标准都关联到同一个场馆
              if (pricing.getVenue() == null || pricing.getVenue().getId() == null) {
                Venue venue = new Venue();
                venue.setId(req.getVenueId());
                pricing.setVenue(venue);
              }
              return pricing;
            })
            .toList();
      }

      return Result.success(service.replaceByVenueId(req.getVenueId(), venuePricings));
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("UPDATE_ERROR", "更新失败: " + e.getMessage());
    }
  }

  // DTO转Entity的辅助方法
  private VenuePricing convertToEntity(VenuePricingDto.VenuePricingRequest req) {
    VenuePricing venuePricing = new VenuePricing();
    if (req.getId() != null) {
      venuePricing.setId(req.getId());
    }
    if (req.getVenueId() != null) {
      Venue venue = new Venue();
      venue.setId(req.getVenueId());
      venuePricing.setVenue(venue);
    }
    if (req.getDayType() != null) {
      venuePricing.setDayType(req.getDayType());
    }
    if (req.getTimeSlotName() != null) {
      venuePricing.setTimeSlotName(req.getTimeSlotName());
    }
    if (req.getStartTime() != null && !req.getStartTime().trim().isEmpty()) {
      try {
        venuePricing.setStartTime(LocalTime.parse(req.getStartTime().trim(), TIME_FORMATTER));
      } catch (Exception e) {
        throw new IllegalArgumentException("开始时间格式错误，请使用HH:mm格式，如06:00");
      }
    }
    if (req.getEndTime() != null && !req.getEndTime().trim().isEmpty()) {
      try {
        venuePricing.setEndTime(LocalTime.parse(req.getEndTime().trim(), TIME_FORMATTER));
      } catch (Exception e) {
        throw new IllegalArgumentException("结束时间格式错误，请使用HH:mm格式，如12:00");
      }
    }
    if (req.getPrice() != null) {
      venuePricing.setPrice(req.getPrice());
    }
    if (req.getUnit() != null) {
      venuePricing.setUnit(req.getUnit());
    }
    if (req.getDescription() != null) {
      venuePricing.setDescription(req.getDescription());
    }
    return venuePricing;
  }
}
