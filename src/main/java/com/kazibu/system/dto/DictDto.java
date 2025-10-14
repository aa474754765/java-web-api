package com.kazibu.system.dto;

public class DictDto {

  public static class DictRequest {
    private Long id;
    private String dictName;
    private String dictType;
    private String remark;
    private String status; // "0" or "1"

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

  public static class DictQuery {
    private String dictName;
    private String status; // optional

    public String getDictName() {
      return dictName;
    }

    public void setDictName(String dictName) {
      this.dictName = dictName;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }

  public static class DictValueRequest {
    private Long id;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private String remark;
    private String status; // "0" or "1"

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

  public static class DictValueQuery {
    private String dictType; // required for list

    public String getDictType() {
      return dictType;
    }

    public void setDictType(String dictType) {
      this.dictType = dictType;
    }
  }
}
