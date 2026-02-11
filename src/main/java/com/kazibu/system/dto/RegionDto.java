package com.kazibu.system.dto;

public class RegionDto {

  /**
   * 查询请求：传入 parentId 查询下一级
   * parentId 为空或0时查询省份列表
   */
  public static class RegionRequest {
    private String parentId;

    public String getParentId() {
      return parentId;
    }

    public void setParentId(String parentId) {
      this.parentId = parentId;
    }
  }

  /**
   * 查询响应：区域信息
   */
  public static class RegionItem {
    private String id;
    private String name;
    private String parentId;
    private Integer level;

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
}
