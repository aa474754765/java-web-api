package com.kazibu.auth.service;

import com.kazibu.auth.entity.Role;
import com.kazibu.auth.dto.RoleResponse;
import com.kazibu.auth.dto.RoleRequest;
import com.kazibu.auth.dto.MenuInfo;
import java.util.List;

public interface RoleService {
  // 1. 查询所有角色（不包含菜单信息）
  List<RoleResponse> getAllRoles();

  // 1.1. 查询所有角色（包含菜单信息）
  List<RoleResponse> getAllRolesWithMenus();

  // 2. 根据角色ID获取菜单列表
  List<MenuInfo> getRoleMenus(Long roleId);

  // 3. 创建角色
  boolean createRole(RoleRequest request);

  // 4. 编辑角色
  boolean updateRole(RoleRequest request);
}