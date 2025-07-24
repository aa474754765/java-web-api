package com.kazibu.auth.service.impl;

import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.repository.MenuRepository;
import com.kazibu.auth.service.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
  @Autowired
  private MenuRepository menuRepository;

  @Override
  public Menu addMenu(Menu menu) {
    return menuRepository.save(menu);
  }

  @Override
  public Menu updateMenu(Menu menu) {
    return menuRepository.save(menu);
  }

  @Override
  public void deleteMenu(Long id) {
    menuRepository.deleteById(id);
  }

  @Override
  public Menu getMenuById(Long id) {
    return menuRepository.findById(id).orElse(null);
  }

  @Override
  public List<Menu> getMenusByParentId(Long parentId) {
    return menuRepository.findAll().stream()
        .filter(menu -> Objects.equals(menu.getParentId(), parentId))
        .collect(Collectors.toList());
  }

  @Override
  public List<Menu> getAllMenusWithChildren() {
    List<Menu> allMenus = menuRepository.findAll();
    Map<Long, Menu> menuMap = allMenus.stream().collect(Collectors.toMap(Menu::getId, m -> m));
    List<Menu> roots = new ArrayList<>();
    for (Menu menu : allMenus) {
      if (menu.getParentId() == null) {
        roots.add(menu);
      } else {
        Menu parent = menuMap.get(menu.getParentId());
        if (parent != null) {
          parent.getChildren().add(menu);
        }
      }
    }
    return roots;
  }
}