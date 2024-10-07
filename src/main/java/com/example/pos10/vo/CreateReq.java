package com.example.pos10.vo;

import java.util.List;

import javax.validation.Valid;

import com.example.pos10.entity.MenuItems;

public class CreateReq {
	
	@Valid
	private List<MenuItems> menuList;
	
	public CreateReq() {
		super();
	}
	
	public List<MenuItems> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuItems> menuList) {
		this.menuList = menuList;
	}
	
	
}
