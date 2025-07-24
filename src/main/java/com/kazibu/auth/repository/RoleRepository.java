package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  java.util.Optional<Role> findByName(String name);

  java.util.List<Role> findByNameContaining(String name);

}
