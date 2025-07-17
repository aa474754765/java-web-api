package com.kazibu.web_api.repository;

import com.kazibu.web_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
