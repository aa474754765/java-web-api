package com.kazibu.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.repository.UserRoleRepository;

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
        .authorities("ROLE_USER") // 可根据实际角色表做扩展
        .build();
  }

  // 下面的代码为UserDetailsServiceImpl类的自定义扩展方法和实现，主要用于用户认证、用户创建、角色分配等功能。
  // 通过注入的UserRepository、RoleRepository和UserRoleRepository实现对用户和角色的数据库操作。
  public boolean usernameExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  /**
   * 检查指定用户名是否已存在
   * @param username 用户名
   * @return 如果用户名已存在返回true，否则返回false
   */
  public void createUser(String username, String encodedPassword) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(encodedPassword);
    // 设置其他默认值
    user.setEnabled(true);

    userRepository.save(user);

    // 分配默认角色 USER
    roleRepository.findByName("USER").ifPresent(role -> {
      UserRole userRole = new UserRole();
      userRole.setUserId(user.getId());
      userRole.setRoleId(role.getId());
      userRoleRepository.save(userRole);
    });
  }

  /**
   * 判断指定用户名是否存在于数据库中
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
        roleRepository.findById(ur.getRoleId()).ifPresent(role -> roles.add(role.getName()));
    }
    return roles;
}
}
