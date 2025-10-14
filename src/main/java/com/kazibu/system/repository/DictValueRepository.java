package com.kazibu.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.system.entity.DictValue;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DictValueRepository extends JpaRepository<DictValue, Long> {
  List<DictValue> findByDictType(String dictType);

  void deleteByDictType(String dictType);

  boolean existsByDictTypeAndDictValue(String dictType, String dictValue);

  @Modifying
  @Query("UPDATE DictValue dv SET dv.dictType = :newType WHERE dv.dictType = :oldType")
  int bulkUpdateDictType(@Param("oldType") String oldType, @Param("newType") String newType);
}
