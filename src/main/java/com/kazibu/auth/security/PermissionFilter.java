package com.kazibu.auth.security;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.entity.UserRole;
import com.kazibu.auth.entity.RoleMenu;
import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.auth.repository.UserRoleRepository;
import com.kazibu.auth.repository.RoleMenuRepository;
import com.kazibu.auth.repository.MenuRepository;
import com.kazibu.system.entity.Result;
import com.kazibu.system.enumData.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import com.kazibu.auth.entity.Role;

@Component
public class PermissionFilter extends OncePerRequestFilter {

  private static final List<String> WHITE_LIST = List.of(
      "/login",
      "/register",
      "/resetPassword",
      "/logout");

  // 添加缓存
  private final ConcurrentHashMap<String, Set<String>> userPermissionsCache = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, User> userCache = new ConcurrentHashMap<>();

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserRoleRepository userRoleRepository;
  @Autowired
  private RoleMenuRepository roleMenuRepository;
  @Autowired
  private MenuRepository menuRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    // 获取当前请求的path
    String requestPath = request.getRequestURI();

    // 检查白名单或swagger开头的路径，或者v3-docs开头的路径，或者uploads开头的路径
    if (WHITE_LIST.contains(requestPath)
        || requestPath.startsWith("/swagger")
        || requestPath.startsWith("/v3/api-docs")
        || requestPath.startsWith("/uploads")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // 获取当前登录用户
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      // 添加调试信息
      System.out.println("=== PermissionFilter Debug ===");
      System.out.println("Request Path: " + requestPath);
      System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));
      System.out.println("Is Authenticated: " + (authentication != null ? authentication.isAuthenticated() : "false"));

      if (authentication == null || !authentication.isAuthenticated()) {
        System.out.println("Sending 403 response for unauthenticated request");
        sendErrorResponse(response,
            Result.error(ErrorCode.NO_PERMISSION.getCode(),
                "未认证，禁止访问接口: " + requestPath + "，请先登录获取token"));
        return;
      }

      // 检查是否是匿名用户
      if ("anonymousUser".equals(authentication.getName())) {
        sendErrorResponse(response,
            Result.error(ErrorCode.NO_PERMISSION.getCode(),
                "匿名用户，禁止访问接口: " + requestPath + "，请先登录获取token"));
        return;
      }

      String username = authentication.getName();
      // 先从缓存获取用户
      User user = userCache.get(username);
      if (user == null) {
        user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
          sendErrorResponse(response,
              Result.error(ErrorCode.NO_PERMISSION.getCode(),
                  "用户不存在: " + username + "，接口: " + requestPath));
          return;
        }
        // 放入缓存
        userCache.put(username, user);
      }

      // 检查用户是否被禁用
      if (!user.getEnabled()) {
        sendErrorResponse(response,
            Result.error(ErrorCode.NO_PERMISSION.getCode(),
                "用户已被禁用: " + username + "，接口: " + requestPath));
        return;
      }

      // 先从缓存获取权限
      Set<String> allowedPaths = userPermissionsCache.get(username);
      if (allowedPaths == null) {
        // 获取用户所有角色
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());
        List<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
          Role role = userRole.getRole();
          // 检查角色状态
          if (role.getStatus() != null && !role.getStatus()) {
            continue; // 跳过禁用的角色
          }
          roleIds.add(role.getId());
        }

        // 如果没有有效角色，返回无权限
        // if (roleIds.isEmpty()) {
        // sendErrorResponse(response,
        // Result.error(ErrorCode.NO_PERMISSION.getCode(),
        // "用户无有效角色: " + username + "，接口: " + requestPath));
        // return;
        // }

        // 获取角色对应的所有菜单
        List<RoleMenu> roleMenus = roleMenuRepository.findAllByRoleIdIn(roleIds);
        Set<Long> menuIds = new HashSet<>();
        for (RoleMenu roleMenu : roleMenus) {
          menuIds.add(roleMenu.getMenu().getId());
        }

        List<Menu> menus = menuRepository.findAllById(menuIds);
        allowedPaths = menus.stream().map(menu -> menu.getPath()).collect(Collectors.toSet());
        // 放入缓存
        userPermissionsCache.put(username, allowedPaths);
      }

      // 判断是否有权限访问当前接口（暂时注释掉，用于测试）
      // if (!allowedPaths.contains(requestPath)) {
      // sendErrorResponse(response,
      // Result.error(ErrorCode.NO_PERMISSION.getCode(),
      // "无权限访问接口: " + requestPath + "，用户: " + username +
      // "，允许的接口: " + String.join(", ", allowedPaths)));
      // return;
      // }

      // 权限验证通过，继续处理请求
      filterChain.doFilter(request, response);

    } catch (Exception e) {
      sendErrorResponse(response,
          Result.error(ErrorCode.SYSTEM_ERROR.getCode(),
              "系统错误，接口: " + requestPath + "，错误: " + e.getMessage()));
    }
  }

  private void sendErrorResponse(HttpServletResponse response, Result<?> result) throws IOException {
    System.out.println("=== sendErrorResponse Debug ===");
    System.out.println("Result: " + result);

    // 重置响应
    response.reset();

    // 设置状态码和内容类型
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");

    // 添加CORS头，避免跨域问题
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

    // 确保响应不被缓存
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");

    // 序列化响应
    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    String jsonResponse = objectMapper.writeValueAsString(result);

    System.out.println("JSON Response: " + jsonResponse);
    System.out.println("Response Status: " + response.getStatus());
    System.out.println("Response Content-Type: " + response.getContentType());

    // 写入响应并刷新
    response.getWriter().write(jsonResponse);
    response.getWriter().flush();

    // 确保响应完成
    response.getWriter().close();

    System.out.println("Response sent successfully");
  }

  /**
   * 清理用户权限缓存
   */
  public void clearUserPermissionsCache(String username) {
    if (username != null) {
      userPermissionsCache.remove(username);
      userCache.remove(username);
    }
  }

  /**
   * 清理所有缓存
   */
  public void clearAllCache() {
    userPermissionsCache.clear();
    userCache.clear();
  }
}