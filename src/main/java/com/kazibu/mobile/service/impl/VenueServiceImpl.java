package com.kazibu.mobile.service.impl;

import com.kazibu.mobile.dto.VenueDto;
import com.kazibu.mobile.service.VenueService;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.entity.VenueImage;
import com.kazibu.sports.repository.VenueRepository;
import com.kazibu.sports.repository.VenueImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("mobileVenueService")
public class VenueServiceImpl implements VenueService {

  @Autowired
  private VenueRepository venueRepository;

  @Autowired
  private VenueImageRepository venueImageRepository;

  @Value("${photo.upload.url-prefix:/uploads}")
  private String urlPrefix;

  @Override
  public VenueDto.PageResponse<VenueDto.VenueListItem> getVenueList(int page, int size) {
    // 确保每页最多10条
    int pageSize = Math.min(size, 10);
    // 创建分页请求，按order字段升序排序（order是实体属性名，对应数据库列sort_order）
    Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "order"));

    // 查询所有场馆（不限制enabled状态）
    Page<Venue> venuePage = venueRepository.findAll(pageable);

    // 转换为移动端DTO
    List<VenueDto.VenueListItem> items = venuePage.getContent().stream()
        .map(v -> {
          VenueDto.VenueListItem item = new VenueDto.VenueListItem();
          item.setId(v.getId());
          item.setName(v.getName());
          item.setAddress(v.getAddress());
          item.setRating(v.getRating());

          // 获取场馆的第一张图片
          List<VenueImage> images = venueImageRepository.findByVenueIdOrderByOrderAsc(v.getId());
          if (images != null && !images.isEmpty()) {
            VenueImage firstImage = images.get(0);
            // 构建图片URL，优先使用fileUrl（fileUrl已经是完整URL，如 "/uploads/venue/xxx.png"）
            String imageUrl = firstImage.getFileUrl();
            if (imageUrl == null || imageUrl.isEmpty()) {
              // 如果没有fileUrl，使用filePath构建URL
              // filePath通常是绝对路径，需要提取相对路径部分（venue/xxx.png）
              String filePath = firstImage.getFilePath();
              if (filePath != null && !filePath.isEmpty()) {
                // 查找 "venue/" 的位置
                int venueIndex = filePath.indexOf("venue/");
                if (venueIndex >= 0) {
                  // 提取 "venue/xxx.png" 部分
                  imageUrl = urlPrefix + "/" + filePath.substring(venueIndex);
                } else {
                  // 如果找不到venue/，使用文件名
                  String fileName = firstImage.getFileName();
                  if (fileName != null && !fileName.isEmpty()) {
                    imageUrl = urlPrefix + "/venue/" + fileName;
                  }
                }
              }
            }
            item.setImageUrl(imageUrl);
          } else {
            item.setImageUrl(null);
          }

          return item;
        })
        .collect(Collectors.toList());

    // 构建分页响应
    VenueDto.PageResponse<VenueDto.VenueListItem> response = new VenueDto.PageResponse<>();
    response.setList(items);
    response.setPage(page);
    response.setSize(pageSize);
    response.setTotal(venuePage.getTotalElements());
    response.setTotalPages(venuePage.getTotalPages());

    return response;
  }
}
