package com.kazibu.auth.service.impl;

import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.entity.User;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.entity.RoleMenu;
import com.kazibu.auth.repository.MenuRepository;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.auth.repository.UserRoleRepository;
import com.kazibu.auth.repository.RoleMenuRepository;
import com.kazibu.auth.service.MenuService;
import com.kazibu.auth.dto.UserInfoDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
  @Autowired
  private MenuRepository menuRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private RoleMenuRepository roleMenuRepository;

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
  public List<Menu> getAllMenus() {
    return menuRepository.findAll();
  }

  @Override
  public List<Menu> getMenusByConditions(Long parentId, String visible, String title) {
    return menuRepository.findByConditions(parentId, visible, title);
  }

  @Override
  public List<Menu> getCurrentUserMenus(String username) {
    // 1. 根据用户名查找用户
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null) {
      return new ArrayList<>();
    }

    // 2. 获取用户的所有角色
    List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());
    Set<Long> roleIds = userRoles.stream()
        .map(userRole -> userRole.getRole().getId())
        .collect(Collectors.toSet());

    if (roleIds.isEmpty()) {
      return new ArrayList<>();
    }

    // 3. 获取这些角色对应的所有菜单
    Set<Long> menuIds = new HashSet<>();
    for (Long roleId : roleIds) {
      List<RoleMenu> roleMenus = roleMenuRepository.findAllByRoleId(roleId);
      for (RoleMenu roleMenu : roleMenus) {
        if (roleMenu.getMenu() != null) {
          menuIds.add(roleMenu.getMenu().getId());
        }
      }
    }

    // 4. 获取菜单详情并返回
    List<Menu> userMenus = new ArrayList<>();
    for (Long menuId : menuIds) {
      Menu menu = menuRepository.findById(menuId).orElse(null);
      if (menu != null) {
        userMenus.add(menu);
      }
    }

    // 5. 按排序字段排序
    userMenus.sort(Comparator.comparing(Menu::getSort, Comparator.nullsLast(Comparator.naturalOrder())));

    return userMenus;
  }

  @Override
  public UserInfoDto getCurrentUserInfo(String username) {
    // 1. 根据用户名查找用户
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null) {
      return null;
    }

    // 2. 创建用户信息DTO
    UserInfoDto userInfo = new UserInfoDto();
    userInfo.setId(user.getId());
    userInfo.setUsername(user.getUsername());
    userInfo.setEnabled(user.getEnabled());
    userInfo.setCreateTime(user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    userInfo.setUpdateTime(user.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    // 3. 获取用户角色
    List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());
    List<String> roles = userRoles.stream()
        .map(userRole -> userRole.getRole().getName())
        .collect(Collectors.toList());
    userInfo.setRoles(roles);

    // 4. 获取用户菜单
    List<Menu> userMenus = getCurrentUserMenus(username);
    userInfo.setMenus(userMenus);

    return userInfo;
  }
}