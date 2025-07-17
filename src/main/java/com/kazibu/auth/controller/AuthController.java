package com.kazibu.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.kazibu.system.entity.Result;
import com.kazibu.system.enumData.ErrorCode;

import com.kazibu.auth.security.JwtUtil;

@RestController
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private com.kazibu.auth.service.UserDetailsServiceImpl userDetailsService;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private PasswordEncoder passwordEncoder;

  // 登陆接口
    @PostMapping("/login")
    public Result<Object> login(@RequestParam String username, @RequestParam String password) {
      try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);

        // 获取用户信息
        // 获取用户角色
        List<String> roles = userDetailsService.getUserRolesByUserName(username);

        // 构建返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("roles", roles);

        return Result.success(data);
      } catch (BadCredentialsException e) {
        return Result.error(ErrorCode.USERNAME_OR_PASSWORD_ERROR.getCode(),
            ErrorCode.USERNAME_OR_PASSWORD_ERROR.getMsg());
      } catch (Exception e) {
        return Result.error(ErrorCode.LOGIN_FAILED.getCode(), ErrorCode.LOGIN_FAILED.getMsg());
      }
    }

  @PostMapping("/register")
  public Result<String> registerUser(@RequestParam String username, @RequestParam String password) {
    // 检查用户名是否已存在
    if (userDetailsService.usernameExists(username)) {
      return Result.error(ErrorCode.USERNAME_EXISTS.getCode(), ErrorCode.USERNAME_EXISTS.getMsg());
    }

    // 创建新用户
    userDetailsService.createUser(
        username,
        passwordEncoder.encode(password)
    // 其他字段
    );

    return Result.success("用户注册成功！");
  }
}
