package com.kazibu.auth.controller;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.service.UserService;
import com.kazibu.auth.dto.UserDto;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.entity.Role;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.repository.UserRoleRepository;
import com.kazibu.auth.security.RequiresPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/user_management")
@Tag(name = "用户管理", description = "用户的增删改查接口")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private RoleRepository roleRepository;

  @PostMapping("/edit")
  @RequiresPermission("system:role:edit")
  public Result<UserDto.UserResponse> updateUser(@RequestBody UserDto.UserRequest request) {
    User existingUser = userService.getUserById(request.getId());
    if (existingUser == null) {
      return Result.error("USER_NOT_FOUND", "用户不存在");
    }

    // 更新用户基本信息（状态、昵称、手机号）
    User updatedUser = userService.updateUserInfo(request);

    // 更新用户角色
    if (request.getRoleIds() != null) {
      // 先删除用户现有角色
      List<UserRole> existingUserRoles = userRoleRepository.findAllByUserId(existingUser.getId());
      userRoleRepository.deleteAll(existingUserRoles);

      // 分配新角色
      for (Long roleId : request.getRoleIds()) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null) {
          UserRole userRole = new UserRole();
          userRole.setUser(existingUser);
          userRole.setRole(role);
          userRoleRepository.save(userRole);
        }
      }
    }

    // 返回格式化的用户响应信息
    UserDto.UserResponse userResponse = userService.convertUserToResponse(updatedUser);
    return Result.success(userResponse);
  }

  @PostMapping("/delete")
  @RequiresPermission("system:role:delete")
  public Result<String> deleteUser(@RequestBody UserDto.UserRequest request) {
    try {
      userService.deleteUser(request.getId());
      return Result.success("删除成功");
    } catch (RuntimeException e) {
      return Result.error("DELETE_FORBIDDEN", e.getMessage());
    }
  }

  @PostMapping("/list")
  @RequiresPermission("system:role:list")
  public Result<List<UserDto.UserResponse>> getUsers(@RequestBody UserDto.UserQuery query) {
    List<UserDto.UserResponse> users;
    if (query == null) {
      // 如果没有查询条件，返回所有用户
      users = userService.getUsersWithRoles(null);
    } else {
      // 根据查询条件过滤用户
      users = userService.getUsersWithRolesByCondition(query.getUsername(), query.getNickName(),
          query.getPhoneNumber(), query.getEnabled());
    }
    return Result.success(users);
  }
}