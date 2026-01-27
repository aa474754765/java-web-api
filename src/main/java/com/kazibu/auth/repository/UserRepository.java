package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kazibu.auth.entity.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  java.util.Optional<User> findByUsername(String username);

  java.util.Optional<User> findByWxOpenId(String wxOpenId);

  // 检查用户名是否存在
  boolean existsByUsername(String username);

  java.util.List<User> findByUsernameContaining(String username);

  java.util.List<User> findByEnabledTrue();

  java.util.List<User> findByUsernameContainingAndEnabledTrue(String username);

  // 根据条件查询用户
  @Query("SELECT u FROM User u WHERE " +
      "(:enabled IS NULL OR u.enabled = :enabled) AND " +
      "(:username IS NULL OR u.username LIKE %:username%) AND " +
      "(:nickName IS NULL OR u.nickName LIKE %:nickName%) AND " +
      "(:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%)")
  List<User> findByConditions(@Param("username") String username,
      @Param("nickName") String nickName,
      @Param("phoneNumber") String phoneNumber,
      @Param("enabled") Boolean enabled);
}
