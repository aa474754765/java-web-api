package com.kazibu.auth.service.impl;

import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.entity.User;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.entity.RoleMenu;
import com.kazibu.auth.repository.MenuRepository;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.auth.repository.UserRoleRepository;
import com.kazibu.auth.repository.RoleMenuRepository;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.entity.Role;
import com.kazibu.auth.service.MenuService;
import com.kazibu.auth.dto.UserInfoDto;
import com.kazibu.auth.dto.MenuTreeSelect;
import com.kazibu.auth.dto.RouterInfo;
import com.kazibu.auth.dto.RouterMeta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Autowired
  private RoleRepository roleRepository;

  @Override
  @Transactional
  public Menu addMenu(Menu menu) {
    // 保存菜单
    Menu savedMenu = menuRepository.save(menu);

    // 为admin角色自动分配菜单权限
    roleRepository.findByName("admin").ifPresent(adminRole -> {
      // 检查是否已经存在该关联，避免重复
      boolean exists = roleMenuRepository.findAllByRoleId(adminRole.getId()).stream()
          .anyMatch(rm -> rm.getMenu() != null && rm.getMenu().getId().equals(savedMenu.getId()));

      if (!exists) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRole(adminRole);
        roleMenu.setMenu(savedMenu);
        roleMenuRepository.save(roleMenu);
      }
    });

    return savedMenu;
  }

  @Override
  public Menu updateMenu(Menu menu) {
    return menuRepository.save(menu);
  }

  @Override
  @Transactional
  public void deleteMenu(Long id) {
    // 先删除角色与菜单的绑定关系
    roleMenuRepository.deleteByMenuId(id);
    // 再删除菜单
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

    // 4. 获取用户权限
    List<Menu> userMenus = getCurrentUserMenus(username);
    List<String> perms = userMenus.stream()
        .map(Menu::getPerms)
        .filter(perm -> perm != null && !perm.trim().isEmpty())
        .collect(Collectors.toList());
    userInfo.setPerms(perms);

    return userInfo;
  }

  @Override
  public List<MenuTreeSelect> getMenuTreeSelect() {
    // 获取所有菜单
    List<Menu> allMenus = menuRepository.findAll();

    // 按排序字段排序
    allMenus.sort(Comparator.comparing(Menu::getSort, Comparator.nullsLast(Comparator.naturalOrder())));

    // 构建树状结构
    return buildMenuTree(allMenus, null);
  }

  /**
   * 递归构建菜单树
   * 
   * @param allMenus 所有菜单列表
   * @param parentId 父菜单ID，null表示根菜单
   * @return 菜单树列表
   */
  private List<MenuTreeSelect> buildMenuTree(List<Menu> allMenus, Long parentId) {
    List<MenuTreeSelect> treeList = new ArrayList<>();

    for (Menu menu : allMenus) {
      Long currentParentId = menu.getParentId();

      // 如果当前菜单的父ID与传入的parentId匹配
      if (Objects.equals(currentParentId, parentId)) {
        MenuTreeSelect treeSelect = new MenuTreeSelect();
        treeSelect.setId(menu.getId());
        treeSelect.setLabel(menu.getTitle());
        // visible字段：1为true（启用），0为false（禁用）
        treeSelect.setDisabled(!"1".equals(menu.getVisible()));

        // 递归查找子菜单
        List<MenuTreeSelect> children = buildMenuTree(allMenus, menu.getId());
        treeSelect.setChildren(children);

        treeList.add(treeSelect);
      }
    }

    return treeList;
  }

  @Override
  public List<RouterInfo> getUserRouters(String username) {
    // 获取用户的所有菜单
    List<Menu> userMenus = getCurrentUserMenus(username);

    // 过滤掉menuType为F的菜单
    userMenus = userMenus.stream()
        .filter(menu -> !"F".equals(menu.getMenuType()))
        .collect(Collectors.toList());

    // 按排序字段排序
    userMenus.sort(Comparator.comparing(Menu::getSort, Comparator.nullsLast(Comparator.naturalOrder())));

    // 构建路由树状结构
    return buildRouterTree(userMenus, null);
  }

  /**
   * 递归构建路由树
   * 
   * @param allMenus 所有菜单列表
   * @param parentId 父菜单ID，null表示根菜单
   * @return 路由树列表
   */
  private List<RouterInfo> buildRouterTree(List<Menu> allMenus, Long parentId) {
    List<RouterInfo> routerList = new ArrayList<>();

    for (Menu menu : allMenus) {
      Long currentParentId = menu.getParentId();

      // 如果当前菜单的父ID与传入的parentId匹配
      if (Objects.equals(currentParentId, parentId)) {
        RouterInfo routerInfo = convertMenuToRouter(menu);

        // 递归查找子菜单
        List<RouterInfo> children = buildRouterTree(allMenus, menu.getId());
        routerInfo.setChildren(children);

        // 如果有子菜单，设置alwaysShow为true
        if (!children.isEmpty()) {
          routerInfo.setAlwaysShow(true);
        }

        routerList.add(routerInfo);
      }
    }

    return routerList;
  }

  /**
   * 将Menu实体转换为RouterInfo
   */
  private RouterInfo convertMenuToRouter(Menu menu) {
    RouterInfo routerInfo = new RouterInfo();

    // 设置name：如果为空，取path的值首字母大写
    String name = menu.getName();
    if (name == null || name.trim().isEmpty()) {
      String path = menu.getPath();
      if (path != null && !path.trim().isEmpty()) {
        name = capitalizeFirstLetter(path.replaceAll("[^a-zA-Z0-9]", ""));
      } else {
        name = "Menu" + menu.getId();
      }
    }
    routerInfo.setName(name);

    // 设置path：menuType为M时，如果前面没有斜杠，加上斜杠
    String path = menu.getPath();
    if ("M".equals(menu.getMenuType()) && path != null && !path.startsWith("/")) {
      path = "/" + path;
    }
    routerInfo.setPath(path);

    // 设置hidden：visible字段，1为false，0为true
    routerInfo.setHidden(!"1".equals(menu.getVisible()));

    // 设置component：menuType为M时，component的值为Layout
    String component = menu.getComponent();
    if ("M".equals(menu.getMenuType())) {
      component = "Layout";
    }
    routerInfo.setComponent(component);

    // 设置meta
    RouterMeta meta = new RouterMeta();
    meta.setTitle(menu.getTitle());
    meta.setIcon(menu.getIcon());
    // noCache为isCache字段，1为true，0为false
    meta.setNoCache("1".equals(menu.getIsCache()));
    meta.setLink(null); // link返回null
    routerInfo.setMeta(meta);

    return routerInfo;
  }

  /**
   * 首字母大写
   */
  private String capitalizeFirstLetter(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
}