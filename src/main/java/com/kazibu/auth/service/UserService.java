package com.kazibu.auth.service;

import com.kazibu.auth.entity.User;
import java.util.List;

public interface UserService {
  User updateUser(User user);

  User updateUserInfo(com.kazibu.auth.dto.UserDto.UserRequest request);

  User deleteUser(Long id);

  User getUserById(Long id);

  List<User> getAllUsers();

  List<User> getUsersByUsername(String username);

  List<User> getUsersByCondition(String username, String nickName, String phoneNumber, Boolean enabled);

  boolean isAdminUser(String username);

  List<com.kazibu.auth.dto.UserDto.UserResponse> getUsersWithRoles(String username);

  List<com.kazibu.auth.dto.UserDto.UserResponse> getUsersWithRolesByCondition(String username, String nickName,
      String phoneNumber, Boolean enabled);

  com.kazibu.auth.dto.UserDto.UserResponse convertUserToResponse(User user);

  boolean resetPassword(String username, String currentPassword, String newEncodedPassword);
}