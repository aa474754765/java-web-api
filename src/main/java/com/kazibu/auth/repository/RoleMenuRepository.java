package com.kazibu.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.RoleMenu;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
  List<RoleMenu> findAllByRoleIdIn(List<Long> roleIds);
}
