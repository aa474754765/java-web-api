package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
