package com.kazibu.mobile.dto;

import java.util.List;

public class VenueDto {

  // 场馆列表项（移动端简化版）
  public static class VenueListItem {
    private Long id; // 场馆ID
    private String name; // 场馆名称
    private String address; // 场馆地址
    private Double rating; // 评分
    private String imageUrl; // 场馆图片（第一张）

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public Double getRating() {
      return rating;
    }

    public void setRating(Double rating) {
      this.rating = rating;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }
  }

  // 分页响应类
  public static class PageResponse<T> {
    private List<T> list; // 数据列表
    private Integer page; // 当前页码（从0开始）
    private Integer size; // 每页大小
    private Long total; // 总记录数
    private Integer totalPages; // 总页数

    public List<T> getList() {
      return list;
    }

    public void setList(List<T> list) {
      this.list = list;
    }

    public Integer getPage() {
      return page;
    }

    public void setPage(Integer page) {
      this.page = page;
    }

    public Integer getSize() {
      return size;
    }

    public void setSize(Integer size) {
      this.size = size;
    }

    public Long getTotal() {
      return total;
    }

    public void setTotal(Long total) {
      this.total = total;
    }

    public Integer getTotalPages() {
      return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
      this.totalPages = totalPages;
    }
  }

  // 分页请求类
  public static class PageRequest {
    private Integer page = 0; // 页码，从0开始
    private Integer size = 10; // 每页大小，默认10

    public Integer getPage() {
      return page;
    }

    public void setPage(Integer page) {
      this.page = page;
    }

    public Integer getSize() {
      return size;
    }

    public void setSize(Integer size) {
      this.size = size;
    }
  }
}
