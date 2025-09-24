package com.kazibu.auth.dto;

public class RouterMeta {
  private String title;
  private String icon;
  private Boolean noCache;
  private String link;

  public RouterMeta() {
  }

  public RouterMeta(String title, String icon, Boolean noCache, String link) {
    this.title = title;
    this.icon = icon;
    this.noCache = noCache;
    this.link = link;
  }

  // getter和setter
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public Boolean getNoCache() {
    return noCache;
  }

  public void setNoCache(Boolean noCache) {
    this.noCache = noCache;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }
}
