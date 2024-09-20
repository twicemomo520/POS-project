package com.example.pos10.entiey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="options")
public class Options {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="option_id")
	private int optionId;
	
	@Column(name="option_title")
	private String optionTitle;
	
	@Column(name="cg_id")
	private Integer categoryId;
	
	@NotBlank(message="Option content cannot be null or empty !")
	@Column(name="option_content")
	private String optionContent;
	
	@Column(name="extra_price")
	private int extraPrice;

	public Options() {
		super();
	}

	public Options(String optionTitle, int categoryId,
			@NotBlank(message = "Option cannot be null or empty !") String optionContent, int extraPrice) {
		super();
		this.optionTitle = optionTitle;
		this.categoryId = categoryId;
		this.optionContent = optionContent;
		this.extraPrice = extraPrice;
	}

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
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

	public int getExtraPrice() {
		return extraPrice;
	}

	public void setExtraPrice(int extraPrice) {
		this.extraPrice = extraPrice;
	}
	
}
