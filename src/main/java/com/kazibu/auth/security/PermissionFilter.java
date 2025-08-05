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

@Component
public class PermissionFilter extends OncePerRequestFilter {

  private static final List<String> WHITE_LIST = List.of(
      "/login",
      "/register",
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

    // 检查白名单或swagger开头的路径，或者v3-docs开头的路径
    if (WHITE_LIST.contains(requestPath)
        || requestPath.startsWith("/swagger")
        || requestPath.startsWith("/v3/api-docs")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // 获取当前登录用户
      // 如果没有认证信息或者未认证，则禁止请求，返回403
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || !authentication.isAuthenticated()) {
        sendErrorResponse(response, Result.error(ErrorCode.NO_PERMISSION.getCode(), "未认证，禁止访问"));
        return;
      }

      String username = authentication.getName();
      // 先从缓存获取用户
      User user = userCache.get(username);
      if (user == null) {
        user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
          sendErrorResponse(response, Result.error(ErrorCode.NO_PERMISSION.getCode(), "用户不存在"));
          return;
        }
        // 放入缓存
        userCache.put(username, user);
      }

      // 先从缓存获取权限
      Set<String> allowedPaths = userPermissionsCache.get(username);
      if (allowedPaths == null) {
        // 获取用户所有角色
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());
        List<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
          roleIds.add(userRole.getRole().getId());
        }

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

      // 判断是否有权限
      // if (!allowedPaths.contains(requestPath)) {
      // sendErrorResponse(response, Result.error(ErrorCode.NO_PERMISSION.getCode(),
      // "无权限访问该接口"));
      // return;
      // }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      sendErrorResponse(response, Result.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统错误"));
    }
  }

  private void sendErrorResponse(HttpServletResponse response, Result<?> result) throws IOException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json;charset=UTF-8");
    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(result));
    response.getWriter().flush();
  }
}