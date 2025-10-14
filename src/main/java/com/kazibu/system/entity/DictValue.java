package com.kazibu.system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_dict_value", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "dict_type", "dict_value" })
})
public class DictValue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 对应 Dict.dictType（使用字符串关联，不建外键约束）
  @Column(name = "dict_type", nullable = false, length = 100)
  private String dictType;

  @Column(name = "dict_label", nullable = false, length = 100)
  private String dictLabel;

  @Column(name = "dict_value", nullable = false, length = 100)
  private String dictValue;

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

  public String getDictType() {
    return dictType;
  }

  public void setDictType(String dictType) {
    this.dictType = dictType;
  }

  public String getDictLabel() {
    return dictLabel;
  }

  public void setDictLabel(String dictLabel) {
    this.dictLabel = dictLabel;
  }

  public String getDictValue() {
    return dictValue;
  }

  public void setDictValue(String dictValue) {
    this.dictValue = dictValue;
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
