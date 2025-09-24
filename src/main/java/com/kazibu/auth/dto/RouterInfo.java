package com.kazibu.auth.dto;

import java.util.List;
import java.util.ArrayList;

public class RouterInfo {
  private String name;
  private String path;
  private Boolean hidden;
  private String component;
  private Boolean alwaysShow;
  private RouterMeta meta;
  private List<RouterInfo> children;

  public RouterInfo() {
    this.children = new ArrayList<>();
  }

  public RouterInfo(String name, String path, Boolean hidden, String component, RouterMeta meta) {
    this.name = name;
    this.path = path;
    this.hidden = hidden;
    this.component = component;
    this.meta = meta;
    this.children = new ArrayList<>();
  }

  // getter和setter
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public Boolean getAlwaysShow() {
    return alwaysShow;
  }

  public void setAlwaysShow(Boolean alwaysShow) {
    this.alwaysShow = alwaysShow;
  }

  public RouterMeta getMeta() {
    return meta;
  }

  public void setMeta(RouterMeta meta) {
    this.meta = meta;
  }

  public List<RouterInfo> getChildren() {
    return children;
  }

  public void setChildren(List<RouterInfo> children) {
    this.children = children;
  }

  public void addChild(RouterInfo child) {
    this.children.add(child);
  }
}
