package com.kazibu.sports.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kazibu.sports.entity.SportsType;
import com.kazibu.sports.service.SportsTypeService;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.security.RequiresPermission;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/sports_type")
@Tag(name = "体育类型管理", description = "体育类型的增删改查接口")
public class SportsTypeController {

  @Autowired
  private SportsTypeService service;

  @PostMapping("/add")
  @RequiresPermission("sports:type:add")
  public Result<SportsType> add(@RequestBody SportsType req) {
    return Result.success(service.create(req));
  }

  @PostMapping("/edit")
  @RequiresPermission("sports:type:edit")
  public Result<SportsType> edit(@RequestBody SportsType req) {
    return Result.success(service.update(req));
  }

  @PostMapping("/delete")
  @RequiresPermission("sports:type:delete")
  public Result<String> delete(@RequestBody SportsType req) {
    service.delete(req.getId());
    return Result.success("删除成功");
  }

  @PostMapping("/get")
  @RequiresPermission("sports:type:get")
  public Result<SportsType> get(@RequestBody SportsType req) {
    return Result.success(service.get(req.getId()));
  }

  @PostMapping("/list")
  @RequiresPermission("sports:type:list")
  public Result<List<SportsType>> list(@RequestBody(required = false) SportsType req) {
    String type = req == null ? null : req.getType();
    return Result.success(service.list(type));
  }
}
