package com.kazibu.auth.controller;

import com.kazibu.auth.service.RoleService;
import com.kazibu.auth.dto.RoleResponse;
import com.kazibu.auth.security.RequiresPermission;
import com.kazibu.auth.dto.RoleRequest;
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
  @PostMapping("/list")
  @RequiresPermission("system:role:list")
  public Result<List<RoleResponse>> getAllRoles(@RequestBody(required = false) RoleRequest request) {
    List<RoleResponse> roles;
    if (request == null) {
      // 如果没有查询条件，返回所有角色
      roles = roleService.getAllRoles();
    } else {
      // 根据查询条件过滤角色
      roles = roleService.getRolesByCondition(request.getRoleName(), request.getName(), request.getStatus());
    }
    return Result.success(roles);
  }

  // 2. 根据角色ID获取完整角色信息（包含菜单）
  @PostMapping("/get")
  @RequiresPermission("system:role:edit")
  public Result<RoleResponse> getRoleById(@RequestBody RoleRequest request) {
    if (request.getId() == null) {
      return Result.error("INVALID_PARAM", "角色ID不能为空");
    }

    RoleResponse role = roleService.getRoleById(request.getId());
    if (role == null) {
      return Result.error("ROLE_NOT_FOUND", "角色不存在");
    }

    return Result.success(role);
  }

  // 3. 创建角色
  @PostMapping("/create")
  @RequiresPermission("system:role:add")
  public Result<String> createRole(@RequestBody RoleRequest request) {
    if (request.getName() == null || request.getName().trim().isEmpty()) {
      return Result.error("INVALID_PARAM", "角色名称不能为空");
    }
    if (request.getRoleName() == null || request.getRoleName().trim().isEmpty()) {
      return Result.error("INVALID_PARAM", "角色显示名称不能为空");
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
  @RequiresPermission("system:role:edit")
  public Result<String> updateRole(@RequestBody RoleRequest request) {
    if (request.getId() == null) {
      return Result.error("INVALID_PARAM", "角色ID不能为空");
    }
    if (request.getName() == null || request.getName().trim().isEmpty()) {
      return Result.error("INVALID_PARAM", "角色名称不能为空");
    }
    if (request.getRoleName() == null || request.getRoleName().trim().isEmpty()) {
      return Result.error("INVALID_PARAM", "角色显示名称不能为空");
    }

    boolean success = roleService.updateRole(request);
    if (success) {
      return Result.success("更新成功");
    } else {
      return Result.error("UPDATE_FAILED", "更新失败，角色不存在或名称已存在");
    }
  }

  // 5. 删除角色
  @PostMapping("/delete")
  @RequiresPermission("system:role:delete")
  public Result<String> deleteRole(@RequestBody RoleRequest request) {
    if (request.getId() == null) {
      return Result.error("INVALID_PARAM", "角色ID不能为空");
    }

    boolean success = roleService.deleteRole(request);
    if (success) {
      return Result.success("删除成功");
    } else {
      return Result.error("DELETE_FAILED", "删除失败，角色不存在或已被删除");
    }
  }

}