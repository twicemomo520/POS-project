package com.example.pos10.vo;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOptionReq {

	private int categoryId;
	
    private String optionTitle;
    
    private String optionType;
	
	@Valid
	@JsonProperty("options")
	private List<OptionContent> optionList;

	public CreateOptionReq() {
		super();
	}

	public CreateOptionReq(int categoryId, String optionTitle, String optionType, @Valid List<OptionContent> optionList) {
		super();
		this.categoryId = categoryId;
		this.optionTitle = optionTitle;
		this.optionType = optionType;
		this.optionList = optionList;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public List<OptionContent> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<OptionContent> optionList) {
		this.optionList = optionList;
	}

	

}
