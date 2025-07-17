package com.kazibu.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

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

  public boolean usernameExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  // 移除@Override注解，因为父类或接口中没有createUser方法
  public void createUser(String username, String encodedPassword) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(encodedPassword);
    // 设置其他默认值
    user.setEnabled(true);

    userRepository.save(user);
  }
}
