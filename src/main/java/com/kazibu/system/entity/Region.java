package com.kazibu.system.entity;

import jakarta.persistence.*;

/**
 * 省市区行政区划实体
 * level: 1=省/直辖市/自治区, 2=市/地级市, 3=区/县
 * parentId: 0表示顶级（省份），其他为父级区划ID
 */
@Entity
@Table(name = "region", indexes = {
    @Index(name = "idx_region_parent_id", columnList = "parent_id"),
    @Index(name = "idx_region_level", columnList = "level")
})
public class Region {

  @Id
  @Column(name = "id", length = 20)
  private String id; // 行政区划代码，如 "110000"

  @Column(name = "name", nullable = false, length = 100)
  private String name; // 区域名称

  @Column(name = "parent_id", nullable = false, length = 20)
  private String parentId; // 父级ID，省级为"0"

  @Column(name = "level", nullable = false)
  private Integer level; // 层级：1省 2市 3区

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }
}
