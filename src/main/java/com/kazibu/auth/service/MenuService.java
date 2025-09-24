package com.kazibu.auth.service;

import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.dto.MenuTreeSelect;
import com.kazibu.auth.dto.RouterInfo;
import java.util.List;

public interface MenuService {
  Menu addMenu(Menu menu);

  Menu updateMenu(Menu menu);

  void deleteMenu(Long id);

  Menu getMenuById(Long id);

  List<Menu> getMenusByParentId(Long parentId);

  List<Menu> getAllMenus();

  // 根据条件查询菜单
  List<Menu> getMenusByConditions(Long parentId, String visible, String title);

  // 获取当前登录用户的所有菜单
  List<Menu> getCurrentUserMenus(String username);

  // 获取当前登录用户的完整信息（包含用户信息和菜单）
  com.kazibu.auth.dto.UserInfoDto getCurrentUserInfo(String username);

  // 获取菜单树状选择器数据
  List<MenuTreeSelect> getMenuTreeSelect();

  // 获取用户路由信息（树状结构）
  List<RouterInfo> getUserRouters(String username);
}