package com.kazibu.system.repository;

import com.kazibu.system.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {

  /**
   * 根据父级ID查询子级区域列表
   */
  List<Region> findByParentIdOrderByIdAsc(String parentId);
}
