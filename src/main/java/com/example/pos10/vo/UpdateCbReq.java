package com.example.pos10.vo;

import javax.validation.constraints.NotBlank;

import com.example.pos10.entity.ComboItems;

public class UpdateCbReq extends ComboItems{
	
	private String oldComboName;

	public UpdateCbReq() {
		super();
	}

	public UpdateCbReq(String oldComboName, String comboName, String comboDetail, int discountAmount) {
		super(comboName, comboDetail, discountAmount);
		
		this.oldComboName = oldComboName;
	}

	public String getOldComboName() {
		return oldComboName;
	}

	public void setOldComboName(String oldComboName) {
		this.oldComboName = oldComboName;
	}



}
