package com.kazibu.auth.dto;

import java.util.List;
import java.util.ArrayList;

public class MenuTreeSelect {
  private Long id;
  private String label;
  private Boolean disabled;
  private List<MenuTreeSelect> children;

  public MenuTreeSelect() {
    this.children = new ArrayList<>();
  }

  public MenuTreeSelect(Long id, String label, Boolean disabled) {
    this.id = id;
    this.label = label;
    this.disabled = disabled;
    this.children = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Boolean getDisabled() {
    return disabled;
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  public List<MenuTreeSelect> getChildren() {
    return children;
  }

  public void setChildren(List<MenuTreeSelect> children) {
    this.children = children;
  }

  public void addChild(MenuTreeSelect child) {
    this.children.add(child);
  }
}
