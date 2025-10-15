package com.kazibu.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import com.kazibu.sports.entity.SportsType;

@Repository
public interface SportsTypeRepository extends JpaRepository<SportsType, Long> {
  default List<SportsType> findAllOrderBySort() {
    return findAll(Sort.by(Sort.Direction.ASC, "order"));
  }

  List<SportsType> findByTypeContainingIgnoreCaseOrderByOrderAsc(String type);
}
