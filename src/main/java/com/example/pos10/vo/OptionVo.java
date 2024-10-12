package com.example.pos10.vo;

import java.util.List;

public class OptionVo {

	private int categoryId;

	private String optionTitle;

	private String optionType;

	private List<OptionItemVo> optionItems;

	public OptionVo() {
		super();
	}

	public OptionVo(int categoryId, String optionTitle, String optionType, List<OptionItemVo> optionItems) {
		super();
		this.categoryId = categoryId;
		this.optionTitle = optionTitle;
		this.optionType = optionType;
		this.optionItems = optionItems;
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

	public List<OptionItemVo> getOptionItems() {
		return optionItems;
	}

	public void setOptionItems(List<OptionItemVo> optionItems) {
		this.optionItems = optionItems;
	}

}
