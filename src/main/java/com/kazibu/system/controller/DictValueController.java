package com.kazibu.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kazibu.system.entity.DictValue;
import com.kazibu.auth.security.RequiresPermission;
import com.kazibu.system.service.DictService;
import com.kazibu.system.entity.Result;

@RestController
@RequestMapping("/dict_value")
public class DictValueController {

  @Autowired
  private DictService dictService;

  @PostMapping("/list")
  @RequiresPermission("system:dict:value:list")
  public Result<List<DictValue>> list(@RequestBody com.kazibu.system.dto.DictDto.DictValueQuery query) {
    return Result.success(dictService.listDictValuesByType(query.getDictType()));
  }

  @PostMapping("/add")
  @RequiresPermission("system:dict:value:add")
  public Result<DictValue> add(@RequestBody com.kazibu.system.dto.DictDto.DictValueRequest req) {
    DictValue v = new DictValue();
    v.setDictType(req.getDictType());
    v.setDictLabel(req.getDictLabel());
    v.setDictValue(req.getDictValue());
    v.setRemark(req.getRemark());
    v.setStatus(req.getStatus());
    return Result.success(dictService.createDictValue(v));
  }

  @PostMapping("/edit")
  @RequiresPermission("system:dict:value:edit")
  public Result<DictValue> edit(@RequestBody com.kazibu.system.dto.DictDto.DictValueRequest req) {
    DictValue v = new DictValue();
    v.setId(req.getId());
    // 不允许修改 dictType/dictValue
    v.setDictLabel(req.getDictLabel());
    v.setRemark(req.getRemark());
    v.setStatus(req.getStatus());
    return Result.success(dictService.updateDictValue(v));
  }

  @PostMapping("/delete")
  @RequiresPermission("system:dict:value:delete")
  public Result<String> delete(@RequestBody com.kazibu.system.dto.DictDto.DictValueRequest req) {
    dictService.deleteDictValue(req.getId());
    return Result.success("删除成功");
  }

  @PostMapping("/get")
  @RequiresPermission("system:dict:value:get")
  public Result<DictValue> get(@RequestBody com.kazibu.system.dto.DictDto.DictValueRequest req) {
    return Result.success(dictService.getDictValue(req.getId()));
  }
}
