package com.kazibu.auth.controller;

import com.kazibu.auth.entity.Role;
import com.kazibu.auth.service.RoleService;
import com.kazibu.auth.dto.RoleDto;
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

  @PostMapping("/add")
  public Result<Role> addRole(@RequestBody RoleDto.RoleRequest request) {
    Role role = new Role();
    role.setName(request.getName());
    role.setDescription(request.getDescription());

    return Result.success(roleService.addRole(role));
  }

  @PostMapping("/edit")
  public Result<Role> updateRole(@RequestBody RoleDto.RoleRequest request) {
    Role existingRole = roleService.getRoleById(request.getId());
    if (existingRole == null) {
      return Result.error("ROLE_NOT_FOUND", "角色不存在");
    }

    existingRole.setName(request.getName());
    existingRole.setDescription(request.getDescription());

    return Result.success(roleService.updateRole(existingRole));
  }

  @PostMapping("/delete")
  public Result<String> deleteRole(@RequestBody RoleDto.RoleRequest request) {
    roleService.deleteRole(request.getId());
    return Result.success("删除成功");
  }

  @PostMapping("/list")
  public Result<List<Role>> getRoles(@RequestBody RoleDto.RoleQuery query) {
    List<Role> roles = roleService.getRolesByName(query.getName());
    return Result.success(roles);
  }
}