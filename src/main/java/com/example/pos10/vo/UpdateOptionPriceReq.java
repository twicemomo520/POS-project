package com.example.pos10.vo;

import java.util.List;

import javax.validation.Valid;

public class UpdateOptionPriceReq extends CreateOptionReq {

	public UpdateOptionPriceReq() {
		super();
	}

	public UpdateOptionPriceReq(int categoryId, String optionTitle, String optionType,
			@Valid List<OptionContent> optionList) {
		super(categoryId, optionTitle, optionType, optionList);
	}
	
}
