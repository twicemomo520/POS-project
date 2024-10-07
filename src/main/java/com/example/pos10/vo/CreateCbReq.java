package com.example.pos10.vo;

import javax.validation.constraints.NotBlank;

import com.example.pos10.entity.ComboItems;

public class CreateCbReq extends ComboItems {

	public CreateCbReq() {
		super();
	}

	public CreateCbReq(String comboName, String comboDetail, int discountAmount) {
		super(comboName, comboDetail, discountAmount);
	}
	
	
	
}
