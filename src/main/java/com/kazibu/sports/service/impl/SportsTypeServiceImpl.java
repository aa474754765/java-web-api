package com.kazibu.sports.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kazibu.sports.entity.SportsType;
import com.kazibu.sports.repository.SportsTypeRepository;
import com.kazibu.sports.service.SportsTypeService;

@Service
public class SportsTypeServiceImpl implements SportsTypeService {

  @Autowired
  private SportsTypeRepository repository;

  @Override
  @Transactional
  public SportsType create(SportsType sportsType) {
    return repository.save(sportsType);
  }

  @Override
  @Transactional
  public SportsType update(SportsType sportsType) {
    // 简单覆盖更新：需存在主键
    return repository.save(sportsType);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public SportsType get(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<SportsType> list() {
    return repository.findAllOrderBySort();
  }

  @Override
  public List<SportsType> list(String type) {
    if (type == null || type.trim().isEmpty()) {
      return repository.findAllOrderBySort();
    }
    return repository.findByTypeContainingIgnoreCaseOrderByOrderAsc(type.trim());
  }
}
