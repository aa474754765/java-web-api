package com.kazibu.auth.service.impl;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.entity.Role;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.auth.repository.UserRoleRepository;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.service.UserService;
import com.kazibu.auth.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public User updateUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User deleteUser(Long id) {
    User user = userRepository.findById(id).orElse(null);
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    if (isAdminUser(user.getUsername())) {
      throw new RuntimeException("不能删除ADMIN用户");
    }

    // 软删除：设置enabled为false
    user.setEnabled(false);
    return userRepository.save(user);
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findByEnabledTrue();
  }

  @Override
  public List<User> getUsersByUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      return getAllUsers();
    }
    return userRepository.findByUsernameContainingAndEnabledTrue(username);
  }

  @Override
  public boolean isAdminUser(String username) {
    return "ADMIN".equalsIgnoreCase(username);
  }

  @Override
  public List<UserDto.UserResponse> getUsersWithRoles(String username) {
    List<User> users;
    if (username == null || username.trim().isEmpty()) {
      users = getAllUsers();
    } else {
      users = getUsersByUsername(username);
    }

    return users.stream().map(this::convertToUserResponse).collect(java.util.stream.Collectors.toList());
  }

  private UserDto.UserResponse convertToUserResponse(User user) {
    UserDto.UserResponse response = new UserDto.UserResponse();
    response.setId(user.getId());
    response.setUsername(user.getUsername());
    response.setEnabled(user.getEnabled());
    response.setCreateTime(user.getCreateTime());
    response.setUpdateTime(user.getUpdateTime());

    // 获取用户角色
    List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());
    List<UserDto.UserResponse.RoleInfo> roleInfos = userRoles.stream()
        .map(userRole -> {
          UserDto.UserResponse.RoleInfo roleInfo = new UserDto.UserResponse.RoleInfo();
          Role role = userRole.getRole();
          roleInfo.setId(role.getId());
          roleInfo.setName(role.getName());
          roleInfo.setDescription(role.getDescription());
          return roleInfo;
        })
        .collect(java.util.stream.Collectors.toList());

    response.setRoles(roleInfos);
    return response;
  }
}