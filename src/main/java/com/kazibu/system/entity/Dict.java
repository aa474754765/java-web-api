package com.kazibu.system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_dict")
public class Dict {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "dict_name", nullable = false, length = 100)
  private String dictName;

  @Column(name = "dict_type", nullable = false, length = 100, unique = true)
  private String dictType;

  @Column(name = "remark", length = 255)
  private String remark;

  // 字符串 0 或 1
  @Column(name = "status", length = 1, nullable = false)
  private String status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDictName() {
    return dictName;
  }

  public void setDictName(String dictName) {
    this.dictName = dictName;
  }

  public String getDictType() {
    return dictType;
  }

  public void setDictType(String dictType) {
    this.dictType = dictType;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
