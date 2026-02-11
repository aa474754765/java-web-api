package com.kazibu.system.service;

import com.kazibu.system.dto.RegionDto;

import java.util.List;

public interface RegionService {

  /**
   * 根据父级ID查询子级区域列表
   * parentId 为空或0时返回省份列表
   */
  List<RegionDto.RegionItem> getChildren(String parentId);
}
