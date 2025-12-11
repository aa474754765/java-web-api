package com.kazibu.sports.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kazibu.sports.entity.VenuePricing;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.repository.VenuePricingRepository;
import com.kazibu.sports.repository.VenueRepository;
import com.kazibu.sports.service.VenuePricingService;

@Service
public class VenuePricingServiceImpl implements VenuePricingService {

  @Autowired
  private VenuePricingRepository repository;

  @Autowired
  private VenueRepository venueRepository;

  @Override
  @Transactional
  public List<VenuePricing> replaceByVenueId(Long venueId, List<VenuePricing> venuePricings) {
    // 验证场馆是否存在
    Venue venue = venueRepository.findById(venueId)
        .orElseThrow(() -> new IllegalArgumentException("场馆不存在，ID: " + venueId));

    // 删除该场馆的所有现有收费标准
    List<VenuePricing> existingPricings = repository.findByVenueIdOrderByOrderAsc(venueId);
    if (!existingPricings.isEmpty()) {
      repository.deleteAll(existingPricings);
    }

    // 如果新的列表为空，直接返回空列表（相当于删除所有）
    if (venuePricings == null || venuePricings.isEmpty()) {
      return new java.util.ArrayList<>();
    }

    // 验证并保存新的收费标准
    for (VenuePricing venuePricing : venuePricings) {
      // 设置场馆
      venuePricing.setVenue(venue);

      // 验证价格
      if (venuePricing.getPrice() == null || venuePricing.getPrice() < 0) {
        throw new IllegalArgumentException("价格必须大于等于0");
      }
    }

    return repository.saveAll(venuePricings);
  }

  @Override
  public List<VenuePricing> listByVenueId(Long venueId) {
    return repository.findByVenueIdOrderByOrderAsc(venueId);
  }
}
