package com.example.pos10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="options")
@IdClass(value = OptionsId.class) // �h��PK����z
public class Options {
	
	@Id
	@Column(name="option_title")
	private String optionTitle;
	
	@Id
	@Column(name="cg_id")
	private Integer categoryId;
	
	@Id
	@Column(name="option_content")
	private String optionContent;
	
	@Column(name="option_type")
	private String optionType;
	
	@Column(name="extra_price")
	private int extraPrice;

	public Options() {
		super();
	}

	public Options(String optionTitle, Integer categoryId, String optionContent,
			@NotBlank(message = "Option type cannot be null or empty !") String optionType, int extraPrice) {
		super();
		this.optionTitle = optionTitle;
		this.categoryId = categoryId;
		this.optionContent = optionContent;
		this.optionType = optionType;
		this.extraPrice = extraPrice;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public int getExtraPrice() {
		return extraPrice;
	}

	public void setExtraPrice(int extraPrice) {
		this.extraPrice = extraPrice;
	}
	
}
