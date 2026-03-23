package com.kazibu.sports.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.entity.SportsType;
import com.kazibu.sports.entity.VenuePricing;
import com.kazibu.sports.entity.VenueCourt;
import com.kazibu.sports.entity.VenueImage;
import com.kazibu.sports.dto.VenueDto;
import com.kazibu.sports.service.VenueService;
import com.kazibu.sports.service.VenuePricingService;
import com.kazibu.sports.service.VenueCourtService;
import com.kazibu.sports.service.VenueImageService;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.security.RequiresPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/sports_venue")
@Tag(name = "场馆管理", description = "场馆的增删改查接口")
public class VenueController {

  @Autowired
  private VenueService service;

  @Autowired
  private VenuePricingService venuePricingService;

  @Autowired
  private VenueCourtService venueCourtService;

  @Autowired
  private VenueImageService venueImageService;

  @PostMapping("/add")
  @RequiresPermission("sports:venue:add")
  @Operation(summary = "新增场馆", description = "创建新场馆")
  public Result<Venue> add(@RequestBody VenueDto.VenueRequest req) {
    try {
      Venue venue = convertToEntity(req);
      return Result.success(service.create(venue));
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("CREATE_ERROR", "创建失败: " + e.getMessage());
    }
  }

  @PostMapping("/edit")
  @RequiresPermission("sports:venue:edit")
  @Operation(summary = "编辑场馆", description = "更新场馆信息")
  public Result<Venue> edit(@RequestBody VenueDto.VenueRequest req) {
    try {
      Venue venue = convertToEntity(req);
      return Result.success(service.update(venue));
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("UPDATE_ERROR", "更新失败: " + e.getMessage());
    }
  }

  @PostMapping("/delete")
  @RequiresPermission("sports:venue:delete")
  @Operation(summary = "删除场馆", description = "删除场馆记录")
  public Result<String> delete(@RequestBody VenueDto.VenueRequest req) {
    service.delete(req.getId());
    return Result.success("删除成功");
  }

  @PostMapping("/get")
  @RequiresPermission("sports:venue:get")
  @Operation(summary = "获取场馆详情", description = "根据ID获取场馆详细信息，包含收费标准、场地信息和图片列表")
  public Result<VenueDto.VenueDetailResponse> get(@RequestBody VenueDto.VenueRequest req) {
    Venue venue = service.get(req.getId());
    if (venue == null) {
      return Result.error("NOT_FOUND", "场馆不存在");
    }
    // 获取该场馆的所有收费标准
    List<VenuePricing> pricings = venuePricingService.listByVenueId(venue.getId());
    // 获取该场馆的所有场地
    List<VenueCourt> courts = venueCourtService.listByVenueId(venue.getId());
    // 获取该场馆的所有图片
    List<VenueImage> images = venueImageService.listByVenueId(venue.getId());
    VenueDto.VenueDetailResponse response = new VenueDto.VenueDetailResponse(venue, pricings, courts, images);
    return Result.success(response);
  }

  @PostMapping("/list")
  @RequiresPermission("sports:venue:list")
  @Operation(summary = "获取场馆列表", description = "获取场馆列表，支持按名称、体育类型、省份、城市编码、联系类型筛选。参数为空时不使用该条件，可组合多个条件查询")
  public Result<List<VenueDto.VenueListItem>> list(@RequestBody(required = false) VenueDto.VenueRequest req) {
    String name = req == null ? null : req.getName();
    Long sportsTypeId = req == null ? null : req.getSportsTypeId();
    String province = req == null ? null : req.getProvince();
    String cityCode = req == null ? null : req.getCityCode();
    String contactType = req == null ? null : req.getContactType();
    String enabled = req == null ? null : req.getEnabled();
    return Result.success(service.list(name, sportsTypeId, province, cityCode, contactType, enabled));
  }

  // DTO转Entity的辅助方法
  private Venue convertToEntity(VenueDto.VenueRequest req) {
    Venue venue = new Venue();
    if (req.getId() != null) {
      venue.setId(req.getId());
    }
    if (req.getName() != null) {
      venue.setName(req.getName());
    }
    if (req.getDescription() != null) {
      venue.setDescription(req.getDescription());
    }
    if (req.getSportsTypeId() != null) {
      SportsType sportsType = new SportsType();
      sportsType.setId(req.getSportsTypeId());
      venue.setSportsType(sportsType);
    }
    if (req.getProvince() != null) {
      venue.setProvince(req.getProvince());
    }
    if (req.getCity() != null) {
      venue.setCity(req.getCity());
    }
    if (req.getDistrict() != null) {
      venue.setDistrict(req.getDistrict());
    }
    if (req.getAddress() != null) {
      venue.setAddress(req.getAddress());
    }
    if (req.getLatitude() != null) {
      venue.setLatitude(req.getLatitude());
    }
    if (req.getLongitude() != null) {
      venue.setLongitude(req.getLongitude());
    }
    if (req.getContactType() != null) {
      venue.setContactType(req.getContactType());
    }
    if (req.getContactInfo() != null) {
      venue.setContactInfo(req.getContactInfo());
    }
    if (req.getRating() != null) {
      venue.setRating(req.getRating());
    }
    if (req.getOrder() != null) {
      venue.setOrder(req.getOrder());
    }
    if (req.getEnabled() != null) {
      venue.setEnabled(req.getEnabled());
    }
    return venue;
  }
}
