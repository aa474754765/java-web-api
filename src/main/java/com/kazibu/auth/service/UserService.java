package com.kazibu.auth.service;

import com.kazibu.auth.entity.User;
import java.util.List;

public interface UserService {
  User updateUser(User user);

  User deleteUser(Long id);

  User getUserById(Long id);

  List<User> getAllUsers();

  List<User> getUsersByUsername(String username);

  boolean isAdminUser(String username);

  List<com.kazibu.auth.dto.UserDto.UserResponse> getUsersWithRoles(String username);
}