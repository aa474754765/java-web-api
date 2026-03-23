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
  VenueDto.PageResponse<VenueDto.VenueListItem> getVenueList(int page, int size, String cityCode);

  /**
   * 查询场馆详情（移动端）
   *
   * @param id 场馆ID
   * @return 场馆详情（包含收费标准、场地信息和图片列表）
   */
  VenueDto.VenueDetailResponse getVenueDetail(Long id);
}
