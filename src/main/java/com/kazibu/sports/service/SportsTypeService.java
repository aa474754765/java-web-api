package com.kazibu.sports.service;

import java.util.List;
import com.kazibu.sports.entity.SportsType;

public interface SportsTypeService {
  SportsType create(SportsType sportsType);

  SportsType update(SportsType sportsType);

  void delete(Long id);

  SportsType get(Long id);

  List<SportsType> list();

  List<SportsType> list(String type);
}
