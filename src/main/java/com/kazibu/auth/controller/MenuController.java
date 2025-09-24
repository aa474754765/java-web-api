package com.kazibu.auth.controller;

import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.service.MenuService;
import com.kazibu.auth.dto.MenuDto;
import com.kazibu.auth.dto.MenuTreeSelect;
import com.kazibu.auth.dto.UserInfoDto;
import com.kazibu.system.entity.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/menu_management")
@Tag(name = "菜单管理", description = "菜单的增删改查接口")
public class MenuController {

  @Autowired
  private MenuService menuService;

  @PostMapping("/add")
  public Result<Menu> addMenu(@RequestBody MenuDto.MenuRequest request) {
    Menu menu = new Menu();
    menu.setName(request.getName());
    menu.setTitle(request.getTitle());
    menu.setComponent(request.getComponent());
    menu.setPerms(request.getPerms());
    menu.setVisible(request.getVisible() != null ? request.getVisible() : "1");
    menu.setIsCache(request.getIsCache() != null ? request.getIsCache() : "0");
    menu.setMenuType(request.getMenuType());
    menu.setPath(request.getPath());
    menu.setIcon(request.getIcon());
    menu.setSort(request.getSort());

    // 如果有parentId，设置父菜单
    if (request.getParentId() != null) {
      Menu parentMenu = menuService.getMenuById(request.getParentId());
      menu.setParent(parentMenu);
    }

    return Result.success(menuService.addMenu(menu));
  }

  @PostMapping("/edit")
  public Result<Menu> updateMenu(@RequestBody MenuDto.MenuRequest request) {
    Menu existingMenu = menuService.getMenuById(request.getId());
    if (existingMenu == null) {
      return Result.error("MENU_NOT_FOUND", "菜单不存在");
    }

    existingMenu.setName(request.getName());
    existingMenu.setTitle(request.getTitle());
    existingMenu.setComponent(request.getComponent());
    existingMenu.setPerms(request.getPerms());
    if (request.getVisible() != null) {
      existingMenu.setVisible(request.getVisible());
    }
    if (request.getIsCache() != null) {
      existingMenu.setIsCache(request.getIsCache());
    }
    existingMenu.setMenuType(request.getMenuType());
    existingMenu.setPath(request.getPath());
    existingMenu.setIcon(request.getIcon());
    existingMenu.setSort(request.getSort());

    // 处理父菜单ID
    if (request.getParentId() != null) {
      Menu parentMenu = menuService.getMenuById(request.getParentId());
      existingMenu.setParent(parentMenu);
    } else {
      // 如果parentId为null，表示设置为根菜单
      existingMenu.setParent(null);
    }

    return Result.success(menuService.updateMenu(existingMenu));
  }

  @PostMapping("/delete")
  public Result<String> deleteMenu(@RequestBody MenuDto.MenuRequest request) {
    menuService.deleteMenu(request.getId());
    return Result.success("删除成功");
  }

  @PostMapping("/list")
  public Result<List<Menu>> getMenusByParentId(@RequestBody MenuDto.MenuQuery query) {
    List<Menu> menus;

    // 检查是否有任何查询条件
    boolean hasConditions = query.getParentId() != null ||
        (query.getVisible() != null && !query.getVisible().trim().isEmpty()) ||
        (query.getTitle() != null && !query.getTitle().trim().isEmpty());

    if (hasConditions) {
      // 使用条件查询
      menus = menuService.getMenusByConditions(
          query.getParentId(),
          query.getVisible() != null ? query.getVisible().trim() : null,
          query.getTitle() != null ? query.getTitle().trim() : null);
    } else {
      // 没有查询条件，返回所有菜单
      menus = menuService.getAllMenus();
    }

    return Result.success(menus);
  }

  @PostMapping("/get")
  public Result<Menu> getMenuById(@RequestBody MenuDto.MenuRequest request) {
    if (request.getId() == null) {
      return Result.error("INVALID_PARAM", "菜单ID不能为空");
    }

    Menu menu = menuService.getMenuById(request.getId());
    if (menu == null) {
      return Result.error("MENU_NOT_FOUND", "菜单不存在");
    }

    return Result.success(menu);
  }

  // 获取当前登录用户的完整信息（包含用户信息和菜单）
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

  @PostMapping("/menuTreeselect")
  public Result<List<MenuTreeSelect>> getMenuTreeSelect() {
    List<MenuTreeSelect> menuTree = menuService.getMenuTreeSelect();
    return Result.success(menuTree);
  }
}