package com.kazibu.auth.service;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.repository.UserRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .disabled(!user.getEnabled()) // 设置用户是否被禁用
        .authorities("ROLE_USER") // 可根据实际角色表做扩展
        .build();
  }

  // 下面的代码为UserDetailsServiceImpl类的自定义扩展方法和实现，主要用于用户认证、用户创建、角色分配等功能。
  // 通过注入的UserRepository、RoleRepository和UserRoleRepository实现对用户和角色的数据库操作。
  public boolean usernameExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  /**
   * 创建新用户
   * 
   * @param username        用户名
   * @param encodedPassword 加密后的密码
   * @param nickName        用户昵称（可选）
   * @param phoneNumber     手机号码（可选）
   */
  public void createUser(String username, String encodedPassword, String nickName, String phoneNumber) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(encodedPassword);
    // 设置其他默认值
    user.setEnabled(true);

    // 设置可选字段
    if (nickName != null && !nickName.trim().isEmpty()) {
      user.setNickName(nickName.trim());
    }
    if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
      user.setPhoneNumber(phoneNumber.trim());
    }

    userRepository.save(user);

    // 分配默认角色 USER
    roleRepository.findByName("USER").ifPresent(role -> {
      UserRole userRole = new UserRole();
      userRole.setUser(user);
      userRole.setRole(role);
      userRoleRepository.save(userRole);
    });
  }

  /**
   * 重置用户密码
   * 
   * @param username           用户名
   * @param newEncodedPassword 新加密后的密码
   * @return 是否重置成功
   */
  public boolean resetPassword(String username, String newEncodedPassword) {
    if (username == null || username.trim().isEmpty()) {
      return false;
    }

    java.util.Optional<User> userOpt = userRepository.findByUsername(username.trim());
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      user.setPassword(newEncodedPassword);
      userRepository.save(user);
      return true;
    }
    return false;
  }

  /**
   * 判断指定用户名是否存在于数据库中
   * 
   * @param username 用户名
   * @return 如果用户名已存在返回true，否则返回false
   */
  public List<String> getUserRolesByUserName(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    Long userId = user.getId();
    List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
    List<String> roles = new ArrayList<>();
    for (UserRole ur : userRoles) {
      if (ur.getRole() != null) {
        roles.add(ur.getRole().getName());
      }
    }
    return roles;
  }
}
