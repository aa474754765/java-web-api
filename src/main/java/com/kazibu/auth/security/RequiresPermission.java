package com.kazibu.auth.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限验证注解
 * 用于标记需要特定权限才能访问的方法
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
  /**
   * 需要的权限标识
   */
  String value() default "";

  /**
   * 权限标识数组（支持多个权限，满足其中一个即可）
   */
  String[] permissions() default {};

  /**
   * 权限验证模式
   * AND: 需要拥有所有权限
   * OR: 拥有任意一个权限即可
   */
  PermissionMode mode() default PermissionMode.OR;

  enum PermissionMode {
    AND, OR
  }
}
