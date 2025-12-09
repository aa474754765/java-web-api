package com.kazibu.material.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import com.kazibu.material.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {
  default List<Photo> findAllOrderBySort() {
    return findAll(Sort.by(Sort.Direction.ASC, "order"));
  }

  List<Photo> findByCategoryOrderByOrderAsc(String category);

  List<Photo> findByFileNameContainingIgnoreCaseOrderByOrderAsc(String fileName);

  List<Photo> findByFileTypeOrderByOrderAsc(String fileType);

  List<Photo> findByOriginalNameContainingIgnoreCaseOrderByOrderAsc(String originalName);

  // 组合查询方法
  List<Photo> findByCategoryAndFileNameContainingIgnoreCaseOrderByOrderAsc(String category, String fileName);

  List<Photo> findByCategoryAndFileTypeOrderByOrderAsc(String category, String fileType);

  List<Photo> findByCategoryAndOriginalNameContainingIgnoreCaseOrderByOrderAsc(String category, String originalName);

  List<Photo> findByFileNameContainingIgnoreCaseAndFileTypeOrderByOrderAsc(String fileName, String fileType);

  List<Photo> findByFileNameContainingIgnoreCaseAndOriginalNameContainingIgnoreCaseOrderByOrderAsc(String fileName, String originalName);

  List<Photo> findByFileTypeAndOriginalNameContainingIgnoreCaseOrderByOrderAsc(String fileType, String originalName);

  List<Photo> findByCategoryAndFileNameContainingIgnoreCaseAndFileTypeOrderByOrderAsc(String category, String fileName, String fileType);

  List<Photo> findByCategoryAndFileNameContainingIgnoreCaseAndOriginalNameContainingIgnoreCaseOrderByOrderAsc(String category, String fileName, String originalName);

  List<Photo> findByCategoryAndFileTypeAndOriginalNameContainingIgnoreCaseOrderByOrderAsc(String category, String fileType, String originalName);

  List<Photo> findByFileNameContainingIgnoreCaseAndFileTypeAndOriginalNameContainingIgnoreCaseOrderByOrderAsc(String fileName, String fileType, String originalName);

  List<Photo> findByCategoryAndFileNameContainingIgnoreCaseAndFileTypeAndOriginalNameContainingIgnoreCaseOrderByOrderAsc(String category, String fileName, String fileType, String originalName);
}
