package com.kazibu.auth.security;

import com.kazibu.auth.service.MenuService;
import com.kazibu.system.entity.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 权限验证切面
 * 拦截带有@RequiresPermission注解的方法，进行权限验证
 */
@Aspect
@Component
public class PermissionAspect {

  @Autowired
  private MenuService menuService;

  @Around("@annotation(com.kazibu.auth.security.RequiresPermission)")
  public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
    // 获取当前用户认证信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Result.error("NOT_AUTHENTICATED", "用户未登录");
    }

    String username = authentication.getName();

    // 获取方法上的权限注解
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    RequiresPermission permissionAnnotation = method.getAnnotation(RequiresPermission.class);

    if (permissionAnnotation == null) {
      // 如果没有权限注解，直接执行方法
      return joinPoint.proceed();
    }

    // 获取需要的权限
    String[] requiredPermissions = getRequiredPermissions(permissionAnnotation);
    if (requiredPermissions.length == 0) {
      // 如果没有指定权限，直接执行方法
      return joinPoint.proceed();
    }

    // 获取用户权限
    List<String> userPermissions = getUserPermissions(username);

    // 验证权限
    boolean hasPermission = checkUserPermissions(userPermissions, requiredPermissions, permissionAnnotation.mode());

    if (!hasPermission) {
      return Result.error("ACCESS_DENIED", "没有权限访问此接口");
    }

    // 权限验证通过，执行原方法
    return joinPoint.proceed();
  }

  /**
   * 获取需要的权限列表
   */
  private String[] getRequiredPermissions(RequiresPermission annotation) {
    if (annotation.permissions().length > 0) {
      return annotation.permissions();
    } else if (!annotation.value().isEmpty()) {
      return new String[] { annotation.value() };
    }
    return new String[0];
  }

  /**
   * 获取用户权限列表
   */
  private List<String> getUserPermissions(String username) {
    try {
      // 通过MenuService获取用户权限
      List<com.kazibu.auth.entity.Menu> userMenus = menuService.getCurrentUserMenus(username);
      return userMenus.stream()
          .map(menu -> menu.getPerms())
          .filter(perm -> perm != null && !perm.trim().isEmpty())
          .collect(java.util.stream.Collectors.toList());
    } catch (Exception e) {
      // 如果获取权限失败，返回空列表
      return java.util.Collections.emptyList();
    }
  }

  /**
   * 检查用户是否拥有所需权限
   */
  private boolean checkUserPermissions(List<String> userPermissions, String[] requiredPermissions,
      RequiresPermission.PermissionMode mode) {
    if (userPermissions.isEmpty() || requiredPermissions.length == 0) {
      return false;
    }

    if (mode == RequiresPermission.PermissionMode.AND) {
      // AND模式：需要拥有所有权限
      return Arrays.stream(requiredPermissions)
          .allMatch(requiredPerm -> userPermissions.contains(requiredPerm));
    } else {
      // OR模式：拥有任意一个权限即可
      return Arrays.stream(requiredPermissions)
          .anyMatch(requiredPerm -> userPermissions.contains(requiredPerm));
    }
  }
}
