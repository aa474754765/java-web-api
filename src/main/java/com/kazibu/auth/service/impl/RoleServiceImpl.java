package com.kazibu.auth.service.impl;

import com.kazibu.auth.entity.Role;
import com.kazibu.auth.repository.RoleRepository;
import com.kazibu.auth.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role addRole(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public Role updateRole(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public void deleteRole(Long id) {
    roleRepository.deleteById(id);
  }

  @Override
  public Role getRoleById(Long id) {
    return roleRepository.findById(id).orElse(null);
  }

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  @Override
  public List<Role> getRolesByName(String name) {
    if (name == null || name.trim().isEmpty()) {
      return getAllRoles();
    }
    return roleRepository.findByNameContaining(name);
  }
}