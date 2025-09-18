package com.kazibu.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kazibu.auth.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  // 根据条件查询菜单
  @Query("SELECT m FROM Menu m WHERE " +
      "(:parentId IS NULL OR m.parent.id = :parentId) AND " +
      "(:visible IS NULL OR m.visible = :visible) AND " +
      "(:title IS NULL OR m.title LIKE %:title%)")
  List<Menu> findByConditions(@Param("parentId") Long parentId,
      @Param("visible") String visible,
      @Param("title") String title);
}
