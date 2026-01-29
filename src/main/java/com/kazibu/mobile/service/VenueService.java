package com.kazibu.mobile.service;

import com.kazibu.mobile.dto.VenueDto;

public interface VenueService {
  /**
   * 分页查询场馆列表（移动端）
   * 
   * @param page 页码（从0开始）
   * @param size 每页大小
   * @return 分页的场馆列表
   */
  VenueDto.PageResponse<VenueDto.VenueListItem> getVenueList(int page, int size);
}
