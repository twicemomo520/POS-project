package com.example.pos10.vo;

import javax.validation.constraints.NotBlank;

import com.example.pos10.entity.Options;

public class DeleteOptionReq extends Options {

	public DeleteOptionReq() {
		super();
	}

	public DeleteOptionReq(String optionTitle, Integer categoryId, String optionContent,
			@NotBlank(message = "Option type cannot be null or empty !") String optionType, int extraPrice) {
		super(optionTitle, categoryId, optionContent, optionType, extraPrice);
	}
	
}
