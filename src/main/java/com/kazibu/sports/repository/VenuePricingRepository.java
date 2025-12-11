package com.kazibu.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.kazibu.sports.entity.VenuePricing;
import java.util.List;

@Repository
public interface VenuePricingRepository extends JpaRepository<VenuePricing, Long>, JpaSpecificationExecutor<VenuePricing> {
  List<VenuePricing> findByVenueIdOrderByOrderAsc(Long venueId);
  void deleteAllByVenueId(Long venueId);
}

