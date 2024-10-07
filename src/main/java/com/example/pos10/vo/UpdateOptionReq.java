package com.example.pos10.vo;

import javax.validation.constraints.NotBlank;

import com.example.pos10.entity.Options;

public class UpdateOptionReq extends Options {

	public UpdateOptionReq() {
		super();
	}

	public UpdateOptionReq(String optionTitle, Integer categoryId, String optionContent,
			@NotBlank(message = "Option type cannot be null or empty !") String optionType, int extraPrice) {
		super(optionTitle, categoryId, optionContent, optionType, extraPrice);
	}

}
