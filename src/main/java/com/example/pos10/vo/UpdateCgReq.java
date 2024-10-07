package com.example.pos10.vo;

import com.example.pos10.entity.Categories;

public class UpdateCgReq extends Categories {

	public UpdateCgReq() {
		super();
	}

	public UpdateCgReq(int categoryId, String category) {
		super(categoryId, category);
	}
	
}
