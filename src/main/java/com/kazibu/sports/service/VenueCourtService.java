package com.kazibu.sports.service;

import java.util.List;
import com.kazibu.sports.entity.VenueCourt;

public interface VenueCourtService {
  List<VenueCourt> replaceByVenueId(Long venueId, List<VenueCourt> courts);

  List<VenueCourt> listByVenueId(Long venueId);
}

