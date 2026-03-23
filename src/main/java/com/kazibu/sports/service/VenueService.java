package com.kazibu.sports.service;

import java.util.List;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.dto.VenueDto;

public interface VenueService {
  Venue create(Venue venue);

  Venue update(Venue venue);

  void delete(Long id);

  Venue get(Long id);

  List<VenueDto.VenueListItem> list(String name, Long sportsTypeId, String province, String cityCode,
      String contactType, String enabled);
}

