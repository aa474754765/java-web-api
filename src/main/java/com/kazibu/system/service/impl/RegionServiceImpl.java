package com.kazibu.system.service.impl;

import com.kazibu.system.dto.RegionDto;
import com.kazibu.system.entity.Region;
import com.kazibu.system.repository.RegionRepository;
import com.kazibu.system.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionServiceImpl implements RegionService {

  @Autowired
  private RegionRepository regionRepository;

  @Override
  public List<RegionDto.RegionItem> getChildren(String parentId) {
    // parentId 为空或"0"时查询省份（顶级）
    String pid = (parentId == null || parentId.trim().isEmpty() || "0".equals(parentId.trim())) ? "0" : parentId.trim();
    List<Region> regions = regionRepository.findByParentIdOrderByIdAsc(pid);
    return regions.stream().map(region -> {
      RegionDto.RegionItem item = new RegionDto.RegionItem();
      item.setId(region.getId());
      item.setName(region.getName());
      item.setParentId(region.getParentId());
      item.setLevel(region.getLevel());
      return item;
    }).collect(Collectors.toList());
  }
}
