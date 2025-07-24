package com.kazibu.auth.controller;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.service.UserService;
import com.kazibu.auth.dto.UserDto;
import com.kazibu.system.entity.Result;
import com.kazibu.auth.entity.Role;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.repository.UserRoleRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
  public Result<User> updateUser(@RequestBody UserDto.UserRequest request) {
    User existingUser = userService.getUserById(request.getId());
    if (existingUser == null) {
      return Result.error("USER_NOT_FOUND", "用户不存在");
    }

    // 更新用户状态
    if (request.getEnabled() != null) {
      existingUser.setEnabled(request.getEnabled());
    }

    // 更新用户角色
    if (request.getRoleIds() != null) {
      // 先删除用户现有角色
      List<UserRole> existingUserRoles = userRoleRepository.findAllByUserId(existingUser.getId());
      userRoleRepository.deleteAll(existingUserRoles);

      // 分配新角色
      for (Long roleId : request.getRoleIds()) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null && !"ADMIN".equalsIgnoreCase(role.getName())) {
          UserRole userRole = new UserRole();
          userRole.setUser(existingUser);
          userRole.setRole(role);
          userRoleRepository.save(userRole);
        }
      }
    }

    return Result.success(userService.updateUser(existingUser));
  }

  @PostMapping("/delete")
  public Result<String> deleteUser(@RequestBody UserDto.UserRequest request) {
    try {
      userService.deleteUser(request.getId());
      return Result.success("删除成功");
    } catch (RuntimeException e) {
      return Result.error("DELETE_FORBIDDEN", e.getMessage());
    }
  }

  @PostMapping("/list")
  public Result<List<UserDto.UserResponse>> getUsers(@RequestBody UserDto.UserQuery query) {
    List<UserDto.UserResponse> users = userService.getUsersWithRoles(query.getUsername());
    return Result.success(users);
  }
}