package com.kazibu.sports.service;

import java.util.List;
import com.kazibu.sports.entity.Venue;

public interface VenueService {
  Venue create(Venue venue);

  Venue update(Venue venue);

  void delete(Long id);

  Venue get(Long id);

  List<Venue> list(String name, Long sportsTypeId, String city, String contactType);
}

