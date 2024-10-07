package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.MenuItems;

public class MenuListRes extends BasicRes {
	
	private List<MenuItems> menuList;
	
	public MenuListRes() {
		super();
	}

	public MenuListRes(int code, String message) {
		super(code, message);
	}

	public MenuListRes(List<MenuItems> menuList) {
		super();
		this.menuList = menuList;
	}

	public List<MenuItems> getMenuList() {
		return menuList;
	}
	
}
