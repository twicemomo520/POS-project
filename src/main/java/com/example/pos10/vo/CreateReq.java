package com.example.pos10.vo;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.example.pos10.entity.MenuItems;

public class CreateReq {
	
	@Valid
	private List<MenuItems> menuList;
	
	@Valid
	private List<MultipartFile> files;
	
	public CreateReq() {
		super();
	}
	
	public List<MenuItems> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuItems> menuList) {
		this.menuList = menuList;
	}

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	
	
}
