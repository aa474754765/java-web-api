package com.kazibu.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.system.entity.Dict;

public interface DictRepository extends JpaRepository<Dict, Long> {
  boolean existsByDictType(String dictType);

  Dict findByDictType(String dictType);

  List<Dict> findByDictNameContainingAndStatusContaining(String dictName, String status);
}
