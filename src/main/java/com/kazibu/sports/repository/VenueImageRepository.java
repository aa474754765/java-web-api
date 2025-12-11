package com.kazibu.sports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kazibu.sports.entity.VenueImage;
import java.util.List;

@Repository
public interface VenueImageRepository extends JpaRepository<VenueImage, Long> {
  List<VenueImage> findByVenueIdOrderByOrderAsc(Long venueId);
  void deleteAllByVenueId(Long venueId);
}

