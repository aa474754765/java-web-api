package com.kazibu.material.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
/**
 * WebMvcConfig 的作用：
 * 该配置类用于自定义 Spring Boot Web 应用的静态资源映射规则。
 * 主要实现了将上传的图片等静态文件（如 /uploads/** 路径）映射到服务器本地指定目录，
 * 使客户端能够通过指定的 URL 前缀直接访问本地文件系统的上传资源。
 */
// 依赖是 spring-boot-starter-web。只要在 pom.xml 里添加了这个依赖，
// WebMvcConfig 里的相关接口和注解（如 @Configuration、WebMvcConfigurer、ResourceHandlerRegistry 等）就能正常使用。
public class WebMvcConfig implements WebMvcConfigurer {

  // 文件上传目录，可通过配置文件 photo.upload.path 指定，默认为 "uploads"
  @Value("${photo.upload.path:uploads}")
  private String uploadPath;

  // 资源访问URL前缀，可通过配置文件 photo.upload.url-prefix 指定，默认为 "/uploads"
  @Value("${photo.upload.url-prefix:/uploads}")
  private String urlPrefix;

  /**
   * 添加自定义静态资源映射，将 urlPrefix 对应的路径映射到本地的 uploadPath 文件夹。
   * 例如：客户端访问 /uploads/xxx.jpg，
   * 实际会读取服务器本地的 uploads/xxx.jpg 文件返回给客户端。
   */
  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    // 获取本地上传目录的绝对路径（兼容 Windows 和 Linux）
    String uploadDir = Paths.get(uploadPath).toAbsolutePath().toString().replace("\\", "/");
    // 注册静态资源映射
    registry.addResourceHandler(urlPrefix + "/**")
        .addResourceLocations("file:" + uploadDir + "/");
  }
}

