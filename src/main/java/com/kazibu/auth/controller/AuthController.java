package com.kazibu.auth.controller;

import com.kazibu.auth.dto.AuthDto;
import com.kazibu.auth.dto.UserInfoDto;
import com.kazibu.auth.dto.RouterInfo;
import com.kazibu.auth.security.JwtUtil;
import com.kazibu.auth.service.MenuService;
import com.kazibu.auth.service.UserService;
import com.kazibu.auth.service.WeChatService;
import com.kazibu.auth.entity.User;
import com.kazibu.auth.repository.UserRepository;
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
import io.swagger.v3.oas.annotations.Operation;

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
  @Autowired
  private UserService userService;
  @Autowired
  private WeChatService weChatService;
  @Autowired
  private UserRepository userRepository;

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

  @PostMapping("/wechatLogin")
  @Operation(summary = "微信小程序登录", description = "通过微信code登录，如果用户不存在则自动创建")
  public Result<Object> wechatLogin(@RequestBody AuthDto.WeChatLoginRequest request) {
    try {
      // 验证code
      if (request.getCode() == null || request.getCode().trim().isEmpty()) {
        return Result.error("INVALID_REQUEST", "微信code不能为空");
      }

      // 调用微信API获取openid和session_key
      Map<String, Object> wechatSession = weChatService.getWeChatSession(request.getCode().trim());
      String openId = (String) wechatSession.get("openid");
      String unionId = (String) wechatSession.get("unionid");

      if (openId == null || openId.isEmpty()) {
        return Result.error("WECHAT_ERROR", "获取微信用户信息失败");
      }

      // 查找或创建用户
      User user = userRepository.findByWxOpenId(openId).orElse(null);
      boolean isNewUser = false;

      if (user == null) {
        // 新用户，自动创建
        isNewUser = true;
        user = new User();
        user.setWxOpenId(openId);
        user.setWxUnionId(unionId);
        // 生成唯一的用户名（使用wx_前缀 + openid，确保唯一性）
        String baseUsername = "wx_" + openId;
        // 如果用户名已存在，添加时间戳确保唯一
        if (userRepository.existsByUsername(baseUsername)) {
          baseUsername = baseUsername + "_" + System.currentTimeMillis();
        }
        user.setUsername(baseUsername);
        // 设置一个随机密码（微信登录不需要密码）
        user.setPassword(passwordEncoder.encode("WECHAT_USER_" + System.currentTimeMillis()));
        user.setEnabled(true);
        user = userRepository.save(user);
      }

      // 更新微信用户信息（昵称和头像）
      if (request.getNickName() != null && !request.getNickName().trim().isEmpty()) {
        user.setWxNickName(request.getNickName().trim());
        user.setNickName(request.getNickName().trim());
      }
      if (request.getAvatarUrl() != null && !request.getAvatarUrl().trim().isEmpty()) {
        user.setWxAvatarUrl(request.getAvatarUrl().trim());
      }
      if (unionId != null && !unionId.isEmpty()) {
        user.setWxUnionId(unionId);
      }
      user = userRepository.save(user);

      // 生成JWT token
      UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
      String token = jwtUtil.generateToken(userDetails);

      // 获取用户角色
      List<String> roles = userDetailsService.getUserRolesByUserName(user.getUsername());

      // 构建返回数据
      Map<String, Object> data = new HashMap<>();
      data.put("token", token);
      data.put("roles", roles);
      data.put("isNewUser", isNewUser);
      data.put("user", Map.of(
          "id", user.getId(),
          "username", user.getUsername(),
          "nickName", user.getNickName() != null ? user.getNickName() : "",
          "avatarUrl", user.getWxAvatarUrl() != null ? user.getWxAvatarUrl() : ""));

      return Result.success(data);
    } catch (RuntimeException e) {
      return Result.error("WECHAT_LOGIN_ERROR", e.getMessage());
    } catch (Exception e) {
      return Result.error("WECHAT_LOGIN_ERROR", "微信登录失败: " + e.getMessage());
    }
  }

  @PostMapping("/resetPassword")
  @Operation(summary = "重置密码", description = "验证用户名和当前密码后重置为新密码")
  public Result<String> resetPassword(@RequestBody AuthDto.ResetPasswordRequest request) {
    // 验证请求参数
    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      return Result.error("INVALID_REQUEST", "用户名不能为空");
    }
    if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
      return Result.error("INVALID_REQUEST", "当前密码不能为空");
    }
    if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
      return Result.error("INVALID_REQUEST", "新密码不能为空");
    }

    // 重置密码（验证用户名和当前密码是否匹配）
    boolean success = userService.resetPassword(
        request.getUsername().trim(),
        request.getCurrentPassword(),
        passwordEncoder.encode(request.getNewPassword()));

    if (success) {
      return Result.success("密码重置成功！");
    } else {
      return Result.error("PASSWORD_ERROR", "用户名或当前密码错误");
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
