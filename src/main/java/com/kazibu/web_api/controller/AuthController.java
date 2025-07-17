package com.kazibu.web_api.controller;

import com.kazibu.web_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private com.kazibu.web_api.service.UserDetailsServiceImpl userDetailsService;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private PasswordEncoder passwordEncoder;

  // 登陆接口
  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      return jwtUtil.generateToken(userDetails);
    } catch (BadCredentialsException e) {
      return "用户名或密码错误";
    } catch (Exception e) {
      return "登录失败，请稍后重试";
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password) {
    // 检查用户名是否已存在
    if (userDetailsService.usernameExists(username)) {
      return ResponseEntity.badRequest().body("Error: Username is already taken!");
    }

    // 创建新用户
    userDetailsService.createUser(
        username,
        passwordEncoder.encode(password)
    // 其他字段
    );

    return ResponseEntity.ok("User registered successfully!");
  }
}
