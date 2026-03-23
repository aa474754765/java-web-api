package com.kazibu.mobile.controller;

import com.kazibu.auth.security.PublicAccess;
import com.kazibu.mobile.dto.VenueRelayDto;
import com.kazibu.mobile.service.VenueRelayService;
import com.kazibu.system.entity.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mobile")
@Tag(name = "场馆接龙（移动端）", description = "移动端场馆接龙创建、查询、参与、取消接口")
public class VenueRelayController {
  @Autowired
  private VenueRelayService venueRelayService;

  @PostMapping("/venue/relay/create")
  @Operation(summary = "新建场馆接龙", description = "创建场馆接龙，记录创建人信息")
  public Result<Long> createRelay(@RequestBody VenueRelayDto.CreateRequest request) {
    try {
      Long id = venueRelayService.createRelay(request);
      return Result.success(id);
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("CREATE_ERROR", "创建失败: " + e.getMessage());
    }
  }

  @PostMapping("/venue/relay/list")
  @PublicAccess
  @Operation(summary = "分页查询接龙", description = "分页查询场馆接龙列表")
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

  @PostMapping("/venue/relay/join")
  @Operation(summary = "参与接龙", description = "参与接龙并增加已报名人数，满员时返回错误")
  public Result<String> joinRelay(@RequestBody VenueRelayDto.JoinRequest request) {
    try {
      venueRelayService.joinRelay(request);
      return Result.success("参与成功");
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("JOIN_ERROR", "参与失败: " + e.getMessage());
    }
  }

  @PostMapping("/venue/relay/cancel")
  @Operation(summary = "取消参与接龙", description = "仅参与该接龙的用户可取消，并减少已报名人数")
  public Result<String> cancelJoin(@RequestBody VenueRelayDto.CancelRequest request) {
    try {
      venueRelayService.cancelJoin(request);
      return Result.success("取消成功");
    } catch (IllegalArgumentException e) {
      return Result.error("VALIDATION_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("CANCEL_ERROR", "取消失败: " + e.getMessage());
    }
  }
}
