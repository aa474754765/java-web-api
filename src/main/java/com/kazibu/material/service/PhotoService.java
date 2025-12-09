package com.kazibu.material.service;

import java.util.List;
import com.kazibu.material.entity.Photo;

public interface PhotoService {
  Photo uploadBase64(String fileBase64, String fileName, String category, String description) throws Exception;

  Photo update(Photo photo);

  void delete(Long id);

  Photo get(Long id);

  List<Photo> list(String category, String fileName, String fileType, String originalName);
}
