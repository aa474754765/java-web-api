package com.kazibu.auth.service;

import com.kazibu.auth.entity.Role;
import java.util.List;

public interface RoleService {
    Role addRole(Role role);
    Role updateRole(Role role);
    void deleteRole(Long id);
    Role getRoleById(Long id);
    List<Role> getAllRoles();
    List<Role> getRolesByName(String name);
} 