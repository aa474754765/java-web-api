package com.kazibu.auth.controller;

import com.kazibu.auth.entity.Menu;
import com.kazibu.auth.service.MenuService;
import com.kazibu.auth.dto.MenuDto;
import com.kazibu.system.entity.Result;

import org.springframework.beans.factory.annotation.Autowired;
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
    existingMenu.setPath(request.getPath());
    existingMenu.setIcon(request.getIcon());
    existingMenu.setSort(request.getSort());

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
    if (query.getParentId() == null) {
      menus = menuService.getAllMenusWithChildren();
    } else {
      menus = menuService.getMenusByParentId(query.getParentId());
    }
    return Result.success(menus);
  }
}