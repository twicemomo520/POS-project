package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.Categories;

public class CategoryRes extends BasicRes {
	
	private List<Categories> cateResList;

	public CategoryRes() {
		super();
	}

	public CategoryRes(int code, String message) {
		super(code, message);
	}

	public List<Categories> getCateResList() {
		return cateResList;
	}

	public void setCateResList(List<Categories> cateResList) {
		this.cateResList = cateResList;
	}
	
	
	
}
