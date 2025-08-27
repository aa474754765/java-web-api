package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kazibu.auth.entity.Role;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
  java.util.Optional<Role> findByName(String name);

  java.util.List<Role> findByNameContaining(String name);

  // 根据条件查询角色
  @Query("SELECT r FROM Role r WHERE " +
         "(:roleName IS NULL OR r.roleName LIKE %:roleName%) AND " +
         "(:name IS NULL OR r.name LIKE %:name%) AND " +
         "(:status IS NULL OR r.status = :status)")
  List<Role> findByConditions(@Param("roleName") String roleName, 
                             @Param("name") String name, 
                             @Param("status") Boolean status);
}
