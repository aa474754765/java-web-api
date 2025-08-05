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

  // 获取当前登录用户的所有菜单
  List<Menu> getCurrentUserMenus(String username);

  // 获取当前登录用户的完整信息（包含用户信息和菜单）
  com.kazibu.auth.dto.UserInfoDto getCurrentUserInfo(String username);
}