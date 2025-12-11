package com.kazibu.sports.service;

import java.util.List;
import com.kazibu.sports.entity.VenuePricing;

public interface VenuePricingService {
  List<VenuePricing> replaceByVenueId(Long venueId, List<VenuePricing> venuePricings);

  List<VenuePricing> listByVenueId(Long venueId);
}

