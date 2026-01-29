package com.kazibu.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.kazibu.sports.entity.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {
  // 分页查询启用的场馆
  org.springframework.data.domain.Page<Venue> findByEnabled(String enabled, org.springframework.data.domain.Pageable pageable);
}

