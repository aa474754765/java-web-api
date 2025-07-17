package com.kazibu.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  // 只有认证用户才能访问
  @GetMapping("/test/hello")
  public String hello() {
    return "登录成功，欢迎访问受保护的接口！";
  }
}