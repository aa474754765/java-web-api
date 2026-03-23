package com.kazibu.sports.controller;

import com.kazibu.auth.security.RequiresPermission;
import com.kazibu.sports.dto.VenueRelayDto;
import com.kazibu.sports.service.VenueRelayService;
import com.kazibu.system.entity.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("sportsVenueRelayController")
@RequestMapping("/sports_venue_relay")
@Tag(name = "场馆接龙管理", description = "场馆接龙查询接口")
public class VenueRelayController {
  @Autowired
  private VenueRelayService venueRelayService;

  @PostMapping("/list")
  @RequiresPermission("sports:venueRelay:list")
  @Operation(summary = "分页查询场馆接龙", description = "分页查询场馆接龙列表，支持按场馆、城市、日期、状态筛选")
  public Result<VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem>> queryRelayList(
      @RequestBody(required = false) VenueRelayDto.RelayPageRequest request) {
    try {
      return Result.success(venueRelayService.queryRelayList(request));
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("QUERY_ERROR", "查询失败: " + e.getMessage());
    }
  }
}
