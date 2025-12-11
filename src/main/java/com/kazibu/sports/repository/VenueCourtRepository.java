package com.kazibu.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kazibu.sports.entity.VenueCourt;
import java.util.List;

@Repository
public interface VenueCourtRepository extends JpaRepository<VenueCourt, Long> {
  List<VenueCourt> findByVenueIdOrderByOrderAsc(Long venueId);
  void deleteAllByVenueId(Long venueId);
}

