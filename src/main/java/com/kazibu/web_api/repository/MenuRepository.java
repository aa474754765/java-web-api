package com.kazibu.web_api.repository;

import com.kazibu.web_api.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
