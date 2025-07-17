package com.kazibu.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kazibu.auth.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
