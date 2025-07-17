package com.kazibu.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
  List<UserRole> findAllByUserId(Long userId);
}
