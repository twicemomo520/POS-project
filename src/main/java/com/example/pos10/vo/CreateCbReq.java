package com.example.pos10.vo;

import javax.validation.constraints.NotBlank;

import com.example.pos10.entiey.ComboItems;

public class CreateCbReq extends ComboItems {

	public CreateCbReq() {
		super();
	}

	public CreateCbReq(@NotBlank(message = "Combo name cannot be null or empty !!") String comboName,
			String comboDetail, int discountAmount, String comDescription) {
		super(comboName, comboDetail, discountAmount, comDescription);
	}
	
	
	
}
