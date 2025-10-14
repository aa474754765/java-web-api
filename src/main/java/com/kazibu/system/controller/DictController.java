package com.kazibu.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kazibu.system.dto.DictDto;
import com.kazibu.system.entity.Dict;
import com.kazibu.auth.security.RequiresPermission;
import com.kazibu.system.service.DictService;
import com.kazibu.system.entity.Result;

@RestController
@RequestMapping("/dict_management")
public class DictController {

  @Autowired
  private DictService dictService;

  @PostMapping("/list")
  @RequiresPermission("system:dict:list")
  public Result<List<Dict>> list(@RequestBody(required = false) DictDto.DictQuery query) {
    String dictName = query == null ? null : query.getDictName();
    String status = query == null ? null : query.getStatus();
    return Result.success(dictService.listDicts(dictName, status));
  }

  @PostMapping("/add")
  @RequiresPermission("system:dict:add")
  public Result<Dict> add(@RequestBody DictDto.DictRequest req) {
    Dict dict = new Dict();
    dict.setDictName(req.getDictName());
    dict.setDictType(req.getDictType());
    dict.setRemark(req.getRemark());
    dict.setStatus(req.getStatus());
    return Result.success(dictService.createDict(dict));
  }

  @PostMapping("/edit")
  @RequiresPermission("system:dict:edit")
  public Result<Dict> edit(@RequestBody DictDto.DictRequest req) {
    Dict dict = new Dict();
    dict.setId(req.getId());
    dict.setDictName(req.getDictName());
    dict.setDictType(req.getDictType());
    dict.setRemark(req.getRemark());
    dict.setStatus(req.getStatus());
    return Result.success(dictService.updateDict(dict));
  }

  @PostMapping("/delete")
  @RequiresPermission("system:dict:delete")
  public Result<String> delete(@RequestBody DictDto.DictRequest req) {
    dictService.deleteDict(req.getId());
    return Result.success("删除成功");
  }

  @PostMapping("/get")
  @RequiresPermission("system:dict:get")
  public Result<Dict> get(@RequestBody DictDto.DictRequest req) {
    return Result.success(dictService.getDict(req.getId()));
  }
}
