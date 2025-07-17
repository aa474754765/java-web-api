package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  java.util.Optional<User> findByUsername(String username);

  // 检查用户名是否存在
  boolean existsByUsername(String username);
}
