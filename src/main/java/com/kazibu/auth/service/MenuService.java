package com.kazibu.auth.service;

import com.kazibu.auth.entity.Menu;
import java.util.List;

public interface MenuService {
  Menu addMenu(Menu menu);

  Menu updateMenu(Menu menu);

  void deleteMenu(Long id);

  Menu getMenuById(Long id);

  List<Menu> getMenusByParentId(Long parentId);

  List<Menu> getAllMenusWithChildren();
}