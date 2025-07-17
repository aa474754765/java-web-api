package com.kazibu.web_api.repository;

import com.kazibu.web_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  java.util.Optional<User> findByUsername(String username);

  // 检查用户名是否存在
  boolean existsByUsername(String username);
}
