package com.example.pos10.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OptionsId implements Serializable {
	
	private String optionTitle;
	
	private int categoryId;
	
	private String optionContent;

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}
	
	
}
