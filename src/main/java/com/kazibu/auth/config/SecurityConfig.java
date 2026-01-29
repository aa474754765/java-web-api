package com.kazibu.auth.config;

import com.kazibu.auth.security.JwtAuthenticationFilter;
import com.kazibu.auth.security.PermissionFilter;
import com.kazibu.auth.security.PublicAccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private PermissionFilter permissionFilter;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private ApplicationContext applicationContext;

  // 存储公开访问路径
  private static final Set<String> PUBLIC_PATHS = ConcurrentHashMap.newKeySet();

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers(
            "/login",
            "/register",
            "/resetPassword",
            "/wechatLogin",
            "/logout",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/uploads/**")
        .permitAll()
        // 动态检查公开访问路径（带 @PublicAccess 注解的接口）
        .requestMatchers(request -> {
          String path = request.getRequestURI();
          Set<String> publicPaths = getPublicPaths();
          if (publicPaths.isEmpty()) {
            return false; // 如果还没扫描完成，返回 false，让后续流程处理
          }
          for (String publicPath : publicPaths) {
            if (path.equals(publicPath)) {
              return true; // 精确匹配
            }
            // 支持通配符匹配
            if (publicPath.endsWith("/**")) {
              String basePath = publicPath.substring(0, publicPath.length() - 3);
              if (path.startsWith(basePath)) {
                return true; // 前缀匹配
              }
            }
          }
          return false; // 不是公开路径，返回 false，继续后续认证流程
        })
        .permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().disable()
        .logout().disable();

    // 加入jwtAuthenticationFilter
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    // 将PermissionFilter放在JWT过滤器之后，确保JWT认证完成后再进行权限检查
    http.addFilterAfter(permissionFilter, JwtAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 应用上下文完全初始化后扫描所有带 @PublicAccess 注解的接口路径
   */
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (event.getApplicationContext() == applicationContext) {
      scanPublicAccessPaths();
    }
  }

  /**
   * 扫描所有带 @PublicAccess 注解的接口路径
   */
  private void scanPublicAccessPaths() {
    try {
      RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
      var handlerMethods = handlerMapping.getHandlerMethods();

      for (var entry : handlerMethods.entrySet()) {
        RequestMappingInfo mappingInfo = entry.getKey();
        HandlerMethod handlerMethod = entry.getValue();

        if (handlerMethod != null) {
          Method method = handlerMethod.getMethod();
          Class<?> controllerClass = handlerMethod.getBeanType();

          // 检查方法或类上是否有 @PublicAccess 注解
          boolean hasPublicAccess = method.isAnnotationPresent(PublicAccess.class)
              || controllerClass.isAnnotationPresent(PublicAccess.class);

          if (hasPublicAccess) {
            // 获取路径模式
            Set<String> patterns = mappingInfo.getPatternValues();
            // 获取类级别的 RequestMapping 路径
            RequestMapping classMapping = controllerClass.getAnnotation(RequestMapping.class);
            String classPath = "";
            if (classMapping != null && classMapping.value().length > 0) {
              classPath = classMapping.value()[0];
            }

            for (String pattern : patterns) {
              // 如果类上有 @RequestMapping，需要拼接类路径和方法路径
              String fullPath = pattern;
              if (!classPath.isEmpty() && !pattern.startsWith(classPath)) {
                // 拼接类路径和方法路径
                if (classPath.endsWith("/") && pattern.startsWith("/")) {
                  fullPath = classPath + pattern.substring(1);
                } else if (!classPath.endsWith("/") && !pattern.startsWith("/")) {
                  fullPath = classPath + "/" + pattern;
                } else {
                  fullPath = classPath + pattern;
                }
              }
              PUBLIC_PATHS.add(fullPath);
            }
          }
        }
      }
    } catch (Exception e) {
      System.err.println("扫描 @PublicAccess 注解失败: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * 获取公开访问路径集合（供 PermissionFilter 使用）
   */
  public static Set<String> getPublicPaths() {
    return new HashSet<>(PUBLIC_PATHS);
  }
}
