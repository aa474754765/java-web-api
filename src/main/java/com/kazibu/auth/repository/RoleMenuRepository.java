package com.kazibu.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.RoleMenu;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
  List<RoleMenu> findAllByRoleIdIn(List<Long> roleIds);

  // 根据角色ID查询角色菜单关联
  List<RoleMenu> findAllByRoleId(Long roleId);

  // 根据角色ID删除角色菜单关联
  void deleteByRoleId(Long roleId);
}
