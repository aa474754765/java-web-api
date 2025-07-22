package com.kazibu.auth.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "role_menu")
public class RoleMenu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 角色
  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Role role;

  // 菜单
  @ManyToOne
  @JoinColumn(name = "menu_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Menu menu;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }
}
