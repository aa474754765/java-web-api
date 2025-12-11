package com.kazibu.sports.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kazibu.sports.entity.VenueCourt;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.repository.VenueCourtRepository;
import com.kazibu.sports.repository.VenueRepository;
import com.kazibu.sports.service.VenueCourtService;

@Service
public class VenueCourtServiceImpl implements VenueCourtService {

  @Autowired
  private VenueCourtRepository repository;

  @Autowired
  private VenueRepository venueRepository;

  @Override
  @Transactional
  public List<VenueCourt> replaceByVenueId(Long venueId, List<VenueCourt> courts) {
    // 验证场馆是否存在
    Venue venue = venueRepository.findById(venueId)
        .orElseThrow(() -> new IllegalArgumentException("场馆不存在，ID: " + venueId));

    // 删除该场馆的所有现有场地
    List<VenueCourt> existingCourts = repository.findByVenueIdOrderByOrderAsc(venueId);
    if (!existingCourts.isEmpty()) {
      repository.deleteAll(existingCourts);
    }

    // 如果新的列表为空，直接返回空列表（相当于删除所有）
    if (courts == null || courts.isEmpty()) {
      return new java.util.ArrayList<>();
    }

    // 设置场馆关联
    for (VenueCourt court : courts) {
      court.setVenue(venue);
    }

    return repository.saveAll(courts);
  }

  @Override
  public List<VenueCourt> listByVenueId(Long venueId) {
    return repository.findByVenueIdOrderByOrderAsc(venueId);
  }
}

