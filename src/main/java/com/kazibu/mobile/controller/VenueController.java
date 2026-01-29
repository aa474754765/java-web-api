package com.kazibu.mobile.controller;

import com.kazibu.mobile.dto.VenueDto;
import com.kazibu.mobile.service.VenueService;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.security.PublicAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("mobileVenueController")
@RequestMapping("/mobile")
@Tag(name = "移动端接口", description = "移动端专用接口，只需登录即可访问")
public class VenueController {

  @Autowired
  private VenueService venueService;

  @PostMapping("/venue/list")
  @PublicAccess
  @Operation(summary = "查询场馆列表", description = "分页查询场馆列表，每页10条，返回场馆名称、地址、评分和第一张图片")
  public Result<VenueDto.PageResponse<VenueDto.VenueListItem>> getVenueList(
      @RequestBody(required = false) VenueDto.PageRequest request) {
    try {
      // 默认值：第0页，每页10条
      int page = (request != null && request.getPage() != null) ? request.getPage() : 0;
      int size = (request != null && request.getSize() != null) ? request.getSize() : 10;

      // 确保每页最多10条
      size = Math.min(size, 10);

      VenueDto.PageResponse<VenueDto.VenueListItem> response = venueService.getVenueList(page, size);
      return Result.success(response);
    } catch (Exception e) {
      return Result.error("QUERY_ERROR", "查询失败: " + e.getMessage());
    }
  }
}
