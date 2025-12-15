package com.kazibu.sports.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.entity.SportsType;
import com.kazibu.sports.dto.VenueDto;
import com.kazibu.sports.repository.VenueRepository;
import com.kazibu.sports.repository.SportsTypeRepository;
import com.kazibu.sports.repository.VenueCourtRepository;
import com.kazibu.sports.repository.VenueImageRepository;
import com.kazibu.sports.repository.VenuePricingRepository;
import com.kazibu.sports.service.VenueService;

@Service
public class VenueServiceImpl implements VenueService {

  @Autowired
  private VenueRepository repository;

  @Autowired
  private SportsTypeRepository sportsTypeRepository;

  @Autowired
  private VenueCourtRepository venueCourtRepository;

  @Autowired
  private VenueImageRepository venueImageRepository;

  @Autowired
  private VenuePricingRepository venuePricingRepository;

  @Override
  @Transactional
  public Venue create(Venue venue) {
    // 验证sportsType是否存在
    if (venue.getSportsType() != null && venue.getSportsType().getId() != null) {
      SportsType sportsType = sportsTypeRepository.findById(venue.getSportsType().getId())
          .orElseThrow(() -> new IllegalArgumentException("体育类型不存在"));
      venue.setSportsType(sportsType);
    } else {
      throw new IllegalArgumentException("体育类型不能为空");
    }
    return repository.save(venue);
  }

  @Override
  @Transactional
  public Venue update(Venue venue) {
    Venue existingVenue = repository.findById(venue.getId())
        .orElseThrow(() -> new IllegalArgumentException("场馆不存在"));

    // 更新字段
    if (venue.getName() != null) {
      existingVenue.setName(venue.getName());
    }
    if (venue.getDescription() != null) {
      existingVenue.setDescription(venue.getDescription());
    }
    if (venue.getSportsType() != null && venue.getSportsType().getId() != null) {
      SportsType sportsType = sportsTypeRepository.findById(venue.getSportsType().getId())
          .orElseThrow(() -> new IllegalArgumentException("体育类型不存在"));
      existingVenue.setSportsType(sportsType);
    }
    if (venue.getCity() != null) {
      existingVenue.setCity(venue.getCity());
    }
    if (venue.getAddress() != null) {
      existingVenue.setAddress(venue.getAddress());
    }
    if (venue.getLatitude() != null) {
      existingVenue.setLatitude(venue.getLatitude());
    }
    if (venue.getLongitude() != null) {
      existingVenue.setLongitude(venue.getLongitude());
    }
    if (venue.getContactType() != null) {
      existingVenue.setContactType(venue.getContactType());
    }
    if (venue.getContactInfo() != null) {
      existingVenue.setContactInfo(venue.getContactInfo());
    }
    if (venue.getRating() != null) {
      // 验证评分范围 1-5
      if (venue.getRating() < 1.0 || venue.getRating() > 5.0) {
        throw new IllegalArgumentException("评分必须在1-5之间");
      }
      existingVenue.setRating(venue.getRating());
    }
    if (venue.getOrder() != null) {
      existingVenue.setOrder(venue.getOrder());
    }
    if (venue.getEnabled() != null) {
      existingVenue.setEnabled(venue.getEnabled());
    }

    return repository.save(existingVenue);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    // 先删除关联的场地
    venueCourtRepository.deleteAllByVenueId(id);
    // 先删除关联的图片（包括文件）
    List<com.kazibu.sports.entity.VenueImage> images = venueImageRepository.findByVenueIdOrderByOrderAsc(id);
    for (com.kazibu.sports.entity.VenueImage image : images) {
      try {
        java.nio.file.Path filePath = java.nio.file.Paths.get(image.getFilePath());
        if (java.nio.file.Files.exists(filePath)) {
          java.nio.file.Files.delete(filePath);
        }
      } catch (Exception e) {
        // 记录日志，但不阻止删除
        System.err.println("删除图片文件失败: " + image.getFilePath() + ", " + e.getMessage());
      }
    }
    venueImageRepository.deleteAllByVenueId(id);
    // 删除关联的收费标准
    venuePricingRepository.deleteAllByVenueId(id);
    // 最后删除场馆
    repository.deleteById(id);
  }

  @Override
  public Venue get(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<VenueDto.VenueListItem> list(String name, Long sportsTypeId, String city, String contactType,
      String enabled) {
    // 使用 Specification 动态构建查询条件
    Specification<Venue> spec = (root, query, cb) -> {
      List<Predicate> predicates = new java.util.ArrayList<>();

      // 如果参数不为空，添加对应的查询条件
      if (name != null && !name.trim().isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%"));
      }
      if (sportsTypeId != null) {
        predicates.add(cb.equal(root.get("sportsType").get("id"), sportsTypeId));
      }
      if (city != null && !city.trim().isEmpty()) {
        predicates.add(cb.equal(root.get("city"), city.trim()));
      }
      if (contactType != null && !contactType.trim().isEmpty()) {
        predicates.add(cb.equal(root.get("contactType"), contactType.trim()));
      }
      if (enabled != null && !enabled.trim().isEmpty()) {
        predicates.add(cb.equal(root.get("enabled"), enabled.trim()));
      }

      // 按 order 字段升序排序
      if (query != null) {
        query.orderBy(cb.asc(root.get("order")));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    List<Venue> venues = repository.findAll(spec);

    // 转成列表 DTO，避免直接暴露实体和懒加载字段
    return venues.stream().map(this::toListItem).collect(Collectors.toList());
  }

  private VenueDto.VenueListItem toListItem(Venue venue) {
    VenueDto.VenueListItem dto = new VenueDto.VenueListItem();
    dto.setId(venue.getId());
    dto.setName(venue.getName());
    dto.setDescription(venue.getDescription());
    if (venue.getSportsType() != null) {
      dto.setSportsTypeId(venue.getSportsType().getId());
      dto.setSportsTypeName(venue.getSportsType().getType());
    }
    dto.setCity(venue.getCity());
    dto.setAddress(venue.getAddress());
    dto.setLatitude(venue.getLatitude());
    dto.setLongitude(venue.getLongitude());
    dto.setContactType(venue.getContactType());
    dto.setContactInfo(venue.getContactInfo());
    dto.setRating(venue.getRating());
    dto.setOrder(venue.getOrder());
    dto.setEnabled(venue.getEnabled());
    return dto;
  }
}
