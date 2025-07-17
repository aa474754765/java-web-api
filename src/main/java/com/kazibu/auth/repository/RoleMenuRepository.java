package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.RoleMenu;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
}
