package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  java.util.Optional<User> findByUsername(String username);

  // 检查用户名是否存在
  boolean existsByUsername(String username);

  java.util.List<User> findByUsernameContaining(String username);

  java.util.List<User> findByEnabledTrue();

  java.util.List<User> findByUsernameContainingAndEnabledTrue(String username);
}
