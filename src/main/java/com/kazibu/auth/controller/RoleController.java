package com.kazibu.auth.controller;

import com.kazibu.auth.service.RoleService;
import com.kazibu.auth.dto.RoleResponse;
import com.kazibu.auth.dto.RoleRequest;
import com.kazibu.auth.dto.MenuInfo;
import com.kazibu.system.entity.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/role_management")
@Tag(name = "角色管理", description = "角色的增删改查接口")
public class RoleController {

  @Autowired
  private RoleService roleService;

  // 1. 查询所有角色（不包含菜单信息）
  @PostMapping("/get_all_roles")
  public Result<List<RoleResponse>> getAllRoles() {
    List<RoleResponse> roles = roleService.getAllRoles();
    return Result.success(roles);
  }

  // 2. 根据角色ID或名称获取菜单列表
  @PostMapping("/get_role_menus")
  public Result<List<MenuInfo>> getRoleMenus(@RequestBody RoleRequest request) {
    if (request.getId() == null && (request.getName() == null || request.getName().trim().isEmpty())) {
      return Result.error("INVALID_PARAM", "角色ID或角色名称不能为空");
    }
    List<MenuInfo> menus = roleService.getRoleMenusByIdOrName(request.getId(), request.getName());
    return Result.success(menus);
  }

  // 3. 创建角色
  @PostMapping("/create")
  public Result<String> createRole(@RequestBody RoleRequest request) {
    if (request.getName() == null || request.getName().trim().isEmpty()) {
      return Result.error("INVALID_PARAM", "角色名称不能为空");
    }

    boolean success = roleService.createRole(request);
    if (success) {
      return Result.success("创建成功");
    } else {
      return Result.error("CREATE_FAILED", "创建失败，角色名称可能已存在");
    }
  }

  // 4. 编辑角色
  @PostMapping("/update")
  public Result<String> updateRole(@RequestBody RoleRequest request) {
    if (request.getId() == null) {
      return Result.error("INVALID_PARAM", "角色ID不能为空");
    }
    if (request.getName() == null || request.getName().trim().isEmpty()) {
      return Result.error("INVALID_PARAM", "角色名称不能为空");
    }

    boolean success = roleService.updateRole(request);
    if (success) {
      return Result.success("更新成功");
    } else {
      return Result.error("UPDATE_FAILED", "更新失败，角色不存在或名称已存在");
    }
  }
}