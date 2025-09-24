package com.kazibu.auth.service;

import com.kazibu.auth.dto.RoleResponse;
import com.kazibu.auth.dto.RoleRequest;
import com.kazibu.auth.dto.MenuInfo;
import java.util.List;

public interface RoleService {
  // 1. 查询所有角色（不包含菜单信息）
  List<RoleResponse> getAllRoles();

  // 1.1. 根据条件查询角色（支持roleName、name、status查询）
  List<RoleResponse> getRolesByCondition(String roleName, String name, Boolean status);

  // 1.2. 查询所有角色（包含菜单信息）
  List<RoleResponse> getAllRolesWithMenus();

  // 1.3. 根据角色ID获取完整角色信息（包含菜单）
  RoleResponse getRoleById(Long roleId);

  // 2. 根据角色ID获取菜单列表
  List<MenuInfo> getRoleMenus(Long roleId);

  // 2.1. 根据角色ID或名称获取菜单列表
  List<MenuInfo> getRoleMenusByIdOrName(Long roleId, String roleName);

  // 3. 创建角色
  boolean createRole(RoleRequest request);

  // 4. 编辑角色
  boolean updateRole(RoleRequest request);

  // 5. 删除角色
  boolean deleteRole(RoleRequest request);
}