package com.kazibu.auth.controller;

import com.kazibu.auth.dto.AuthDto;
import com.kazibu.auth.dto.UserInfoDto;
import com.kazibu.auth.dto.RouterInfo;
import com.kazibu.auth.security.JwtUtil;
import com.kazibu.auth.security.RequiresPermission;
import com.kazibu.auth.service.MenuService;
import com.kazibu.system.entity.Result;
import com.kazibu.system.enumData.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "认证管理", description = "用户登录注册接口")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private com.kazibu.auth.service.UserDetailsServiceImpl userDetailsService;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private MenuService menuService;

  // 登陆接口
  @PostMapping("/login")
  public Result<Object> login(@RequestBody AuthDto.LoginRequest request) {
    try {
      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
      String token = jwtUtil.generateToken(userDetails);

      // 获取用户信息
      // 获取用户角色
      List<String> roles = userDetailsService.getUserRolesByUserName(request.getUsername());

      // 构建返回数据
      Map<String, Object> data = new HashMap<>();
      data.put("token", token);
      data.put("roles", roles);

      return Result.success(data);
    } catch (BadCredentialsException e) {
      return Result.error(ErrorCode.USERNAME_OR_PASSWORD_ERROR.getCode(),
          ErrorCode.USERNAME_OR_PASSWORD_ERROR.getMsg());
    } catch (DisabledException e) {
      return Result.error("USER_DISABLED", "用户已被禁用");
    } catch (Exception e) {
      return Result.error(ErrorCode.LOGIN_FAILED.getCode(), ErrorCode.LOGIN_FAILED.getMsg());
    }
  }

  @PostMapping("/register")
  public Result<String> registerUser(@RequestBody AuthDto.RegisterRequest request) {
    // 检查用户名是否已存在
    if (userDetailsService.usernameExists(request.getUsername())) {
      return Result.error(ErrorCode.USERNAME_EXISTS.getCode(), ErrorCode.USERNAME_EXISTS.getMsg());
    }

    // 创建新用户
    userDetailsService.createUser(
        request.getUsername(),
        passwordEncoder.encode(request.getPassword()),
        request.getNickName(),
        request.getPhoneNumber());

    return Result.success("用户注册成功！");
  }

  @PostMapping("/resetPassword")
  @RequiresPermission("system:role:resetPassword")
  public Result<String> resetPassword(@RequestBody AuthDto.ResetPasswordRequest request) {
    // 验证请求参数
    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      return Result.error("INVALID_REQUEST", "用户名不能为空");
    }

    if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
      return Result.error("INVALID_REQUEST", "密码不能为空");
    }

    // 直接尝试重置密码，让服务层处理用户存在性检查
    boolean success = userDetailsService.resetPassword(
        request.getUsername(),
        passwordEncoder.encode(request.getPassword()));

    if (success) {
      return Result.success("密码重置成功！");
    } else {
      return Result.error("USER_NOT_FOUND", "用户不存在");
    }
  }

  // 登出接口
  @PostMapping("/logout")
  public Result<String> logout() {
    try {
      // 获取当前登录用户信息
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || !authentication.isAuthenticated()) {
        return Result.error("NOT_AUTHENTICATED", "用户未登录");
      }

      // 清除Spring Security上下文中的认证信息
      SecurityContextHolder.clearContext();

      // 这里可以添加额外的登出逻辑，比如：
      // 1. 将token加入黑名单
      // 2. 记录登出日志
      // 3. 清除用户会话信息等

      return Result.success("登出成功");
    } catch (Exception e) {
      return Result.error("LOGOUT_FAILED", "登出失败");
    }
  }

  // 获取当前登录用户的完整信息（包含用户信息和权限）
  @PostMapping("/getUserInfo")
  public Result<UserInfoDto> getUserInfo() {
    // 获取当前登录用户信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Result.error("NOT_AUTHENTICATED", "用户未登录");
    }

    String username = authentication.getName();
    UserInfoDto userInfo = menuService.getCurrentUserInfo(username);
    if (userInfo == null) {
      return Result.error("USER_NOT_FOUND", "用户不存在");
    }
    return Result.success(userInfo);
  }

  // 获取当前用户的路由信息（树状结构）
  @PostMapping("/getRouters")
  public Result<List<RouterInfo>> getRouters() {
    // 获取当前登录用户信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Result.error("NOT_AUTHENTICATED", "用户未登录");
    }

    String username = authentication.getName();
    List<RouterInfo> routers = menuService.getUserRouters(username);
    return Result.success(routers);
  }
}
