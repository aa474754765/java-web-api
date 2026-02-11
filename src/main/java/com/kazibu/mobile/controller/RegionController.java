package com.kazibu.mobile.controller;

import com.kazibu.system.dto.RegionDto;
import com.kazibu.system.entity.Result;
import com.kazibu.system.service.RegionService;
import com.kazibu.auth.security.PublicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("mobileRegionController")
@RequestMapping("/mobile")
@Tag(name = "省市区查询（移动端）", description = "移动端省市区行政区划查询接口")
public class RegionController {

  @Autowired
  private RegionService regionService;

  @PostMapping("/region/list")
  @PublicAccess
  @Operation(summary = "查询省市区列表", description = "传入parentId查询下一级，不传或传0查询省份列表")
  public Result<List<RegionDto.RegionItem>> list(
      @RequestBody(required = false) RegionDto.RegionRequest request) {
    try {
      String parentId = (request != null) ? request.getParentId() : null;
      List<RegionDto.RegionItem> list = regionService.getChildren(parentId);
      return Result.success(list);
    } catch (Exception e) {
      return Result.error("QUERY_ERROR", "查询失败: " + e.getMessage());
    }
  }
}
