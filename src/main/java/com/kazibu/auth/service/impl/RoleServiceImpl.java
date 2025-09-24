package com.kazibu.auth.service.impl;

import com.kazibu.auth.entity.Role;
import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.entity.RoleMenu;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.repository.MenuRepository;
import com.kazibu.auth.repository.RoleMenuRepository;
import com.kazibu.auth.service.RoleService;
import com.kazibu.auth.dto.RoleResponse;
import com.kazibu.auth.dto.RoleRequest;
import com.kazibu.auth.dto.MenuInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private MenuRepository menuRepository;

  @Autowired
  private RoleMenuRepository roleMenuRepository;

  @Override
  public List<RoleResponse> getAllRoles() {
    List<Role> roles = roleRepository.findAll();
    return roles.stream()
        .map(this::convertToRoleResponseBasic)
        .collect(Collectors.toList());
  }

  @Override
  public List<RoleResponse> getRolesByCondition(String roleName, String name, Boolean status) {
    List<Role> roles;

    // 如果所有查询条件都为空，返回所有角色
    if ((roleName == null || roleName.trim().isEmpty()) &&
        (name == null || name.trim().isEmpty()) &&
        status == null) {
      roles = roleRepository.findAll();
    } else {
      // 根据条件查询
      roles = roleRepository.findByConditions(
          roleName != null ? roleName.trim() : null,
          name != null ? name.trim() : null,
          status);
    }

    return roles.stream()
        .map(this::convertToRoleResponseBasic)
        .collect(Collectors.toList());
  }

  @Override
  public List<RoleResponse> getAllRolesWithMenus() {
    List<Role> roles = roleRepository.findAll();
    return roles.stream()
        .map(this::convertToRoleResponse)
        .collect(Collectors.toList());
  }

  @Override
  public RoleResponse getRoleById(Long roleId) {
    Role role = roleRepository.findById(roleId).orElse(null);
    if (role == null) {
      return null;
    }
    return convertToRoleResponse(role);
  }

  @Override
  public List<MenuInfo> getRoleMenus(Long roleId) {
    List<RoleMenu> roleMenus = roleMenuRepository.findAllByRoleId(roleId);
    List<MenuInfo> menuInfos = new ArrayList<>();

    for (RoleMenu roleMenu : roleMenus) {
      Menu menu = roleMenu.getMenu();
      if (menu != null) {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setId(menu.getId());
        menuInfo.setName(menu.getName());
        menuInfo.setPath(menu.getPath());
        menuInfo.setIcon(menu.getIcon());
        menuInfo.setSort(menu.getSort());
        menuInfo.setParentId(menu.getParentId());
        menuInfos.add(menuInfo);
      }
    }

    return menuInfos;
  }

  @Override
  @Transactional
  public boolean createRole(RoleRequest request) {
    try {
      // 检查角色名称是否已存在
      if (roleRepository.findByName(request.getName()).isPresent()) {
        return false; // 角色名称已存在
      }

      // 创建角色
      Role role = new Role();
      role.setName(request.getName());
      role.setDescription(request.getDescription());
      role.setStatus(request.getStatus() != null ? request.getStatus() : true);
      role.setRoleName(request.getRoleName() != null ? request.getRoleName() : request.getName());
      role = roleRepository.save(role);

      // 分配菜单
      if (request.getMenuIds() != null && !request.getMenuIds().isEmpty()) {
        assignMenusToRole(role.getId(), request.getMenuIds());
      }

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  @Transactional
  public boolean updateRole(RoleRequest request) {
    try {
      Role existingRole = roleRepository.findById(request.getId()).orElse(null);
      if (existingRole == null) {
        return false; // 角色不存在
      }

      // 检查角色名称是否已被其他角色使用
      roleRepository.findByName(request.getName()).ifPresent(role -> {
        if (!role.getId().equals(request.getId())) {
          throw new RuntimeException("角色名称已存在");
        }
      });

      // 更新角色基本信息
      existingRole.setName(request.getName());
      existingRole.setDescription(request.getDescription());
      if (request.getStatus() != null) {
        existingRole.setStatus(request.getStatus());
      }
      if (request.getRoleName() != null) {
        existingRole.setRoleName(request.getRoleName());
      }
      roleRepository.save(existingRole);

      // 重新分配菜单
      roleMenuRepository.deleteByRoleId(existingRole.getId());
      if (request.getMenuIds() != null && !request.getMenuIds().isEmpty()) {
        assignMenusToRole(existingRole.getId(), request.getMenuIds());
      }

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  @Transactional
  public boolean deleteRole(RoleRequest request) {
    try {
      if (request.getId() == null) {
        return false; // 角色ID不能为空
      }

      Role existingRole = roleRepository.findById(request.getId()).orElse(null);
      if (existingRole == null) {
        return false; // 角色不存在
      }

      // 先删除角色与菜单的关联关系
      roleMenuRepository.deleteByRoleId(existingRole.getId());

      // 删除角色
      roleRepository.deleteById(existingRole.getId());

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 将Role实体转换为RoleResponse（只包含基本信息，不包含菜单）
   */
  private RoleResponse convertToRoleResponseBasic(Role role) {
    RoleResponse response = new RoleResponse();
    response.setId(role.getId());
    response.setName(role.getName());
    response.setDescription(role.getDescription());
    response.setStatus(role.getStatus());
    response.setRoleName(role.getRoleName());
    response.setCreateTime(role.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    response.setUpdateTime(role.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    response.setMenuIds(new ArrayList<>()); // 空菜单ID列表
    return response;
  }

  /**
   * 将Role实体转换为RoleResponse（包含菜单信息）
   */
  private RoleResponse convertToRoleResponse(Role role) {
    RoleResponse response = new RoleResponse();
    response.setId(role.getId());
    response.setName(role.getName());
    response.setDescription(role.getDescription());
    response.setStatus(role.getStatus());
    response.setRoleName(role.getRoleName());
    response.setCreateTime(role.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    response.setUpdateTime(role.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    // 获取角色的菜单ID列表
    List<RoleMenu> roleMenus = roleMenuRepository.findAllByRoleId(role.getId());
    List<Long> menuIds = new ArrayList<>();

    for (RoleMenu roleMenu : roleMenus) {
      Menu menu = roleMenu.getMenu();
      if (menu != null) {
        // 双重检查：确保菜单确实存在
        Menu existingMenu = menuRepository.findById(menu.getId()).orElse(null);
        if (existingMenu != null) {
          menuIds.add(menu.getId());
        }
      }
    }

    response.setMenuIds(menuIds);
    return response;
  }

  /**
   * 为角色分配菜单
   */
  private void assignMenusToRole(Long roleId, List<Long> menuIds) {
    for (Long menuId : menuIds) {
      Menu menu = menuRepository.findById(menuId).orElse(null);
      if (menu != null) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRole(roleRepository.findById(roleId).orElse(null));
        roleMenu.setMenu(menu);
        roleMenuRepository.save(roleMenu);
      }
    }
  }
}