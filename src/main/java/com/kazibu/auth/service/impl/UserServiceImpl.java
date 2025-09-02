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
  public User updateUserInfo(com.kazibu.auth.dto.UserDto.UserRequest request) {
    User existingUser = userRepository.findById(request.getId()).orElse(null);
    if (existingUser == null) {
      throw new RuntimeException("用户不存在");
    }

    // 更新用户状态
    if (request.getEnabled() != null) {
      existingUser.setEnabled(request.getEnabled());
    }

    // 更新用户昵称
    if (request.getNickName() != null) {
      existingUser.setNickName(request.getNickName());
    }

    // 更新手机号码
    if (request.getPhoneNumber() != null) {
      existingUser.setPhoneNumber(request.getPhoneNumber());
    }

    return userRepository.save(existingUser);
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

    // 先删除用户角色关联
    List<UserRole> userRoles = userRoleRepository.findAllByUserId(id);
    if (!userRoles.isEmpty()) {
      userRoleRepository.deleteAll(userRoles);
    }

    // 物理删除用户
    userRepository.delete(user);
    return user;
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
  public List<User> getUsersByCondition(String username, String nickName, String phoneNumber, Boolean enabled) {
    // 如果所有查询条件都为空，返回所有用户
    if ((username == null || username.trim().isEmpty()) &&
        (nickName == null || nickName.trim().isEmpty()) &&
        (phoneNumber == null || phoneNumber.trim().isEmpty()) &&
        enabled == null) {
      return getAllUsers();
    }

    // 根据条件查询
    return userRepository.findByConditions(
        username != null ? username.trim() : null,
        nickName != null ? nickName.trim() : null,
        phoneNumber != null ? phoneNumber.trim() : null,
        enabled);
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

  @Override
  public List<UserDto.UserResponse> getUsersWithRolesByCondition(String username, String nickName, String phoneNumber,
      Boolean enabled) {
    List<User> users = getUsersByCondition(username, nickName, phoneNumber, enabled);
    return users.stream().map(this::convertToUserResponse).collect(java.util.stream.Collectors.toList());
  }

  @Override
  public UserDto.UserResponse convertUserToResponse(User user) {
    return convertToUserResponse(user);
  }

  private UserDto.UserResponse convertToUserResponse(User user) {
    UserDto.UserResponse response = new UserDto.UserResponse();
    response.setId(user.getId());
    response.setUsername(user.getUsername());
    response.setEnabled(user.getEnabled());
    response.setNickName(user.getNickName());
    response.setPhoneNumber(user.getPhoneNumber());

    // 格式化时间为 yyyy-MM-dd HH:mm:ss 格式
    if (user.getCreateTime() != null) {
      response.setCreateTime(
          user.getCreateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    if (user.getUpdateTime() != null) {
      response.setUpdateTime(
          user.getUpdateTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    // 获取用户角色
    List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());
    List<UserDto.UserResponse.RoleInfo> roleInfos = userRoles.stream()
        .map(userRole -> {
          UserDto.UserResponse.RoleInfo roleInfo = new UserDto.UserResponse.RoleInfo();
          Role role = userRole.getRole();
          roleInfo.setId(role.getId());
          roleInfo.setName(role.getName());
          roleInfo.setDescription(role.getDescription());
          roleInfo.setStatus(role.getStatus());
          roleInfo.setRoleName(role.getRoleName());
          return roleInfo;
        })
        .collect(java.util.stream.Collectors.toList());

    response.setRoles(roleInfos);
    return response;
  }
}