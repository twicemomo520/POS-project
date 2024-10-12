package com.example.pos10.vo;

public class OptionItemVo {

	private String optionContent;

	private int extraPrice;

	public OptionItemVo() {
		super();
	}

	public OptionItemVo(String optionContent, int extraPrice) {
		super();
		this.optionContent = optionContent;
		this.extraPrice = extraPrice;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}

	public int getExtraPrice() {
		return extraPrice;
	}

	public void setExtraPrice(int extraPrice) {
		this.extraPrice = extraPrice;
	}

}
