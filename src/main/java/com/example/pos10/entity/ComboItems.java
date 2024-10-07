package com.example.pos10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "combo_items")
public class ComboItems {
	
	@Id
	@NotBlank(message="Combo name cannot be null or empty !!")
	@Column(name = "combo_name")
	private String comboName;

	@NotBlank(message="Combo detail cannot be null or empty !!")
	@Column(name = "combo_detail")
	private String comboDetail;

	@NotNull(message="Discount amount cannot be null !!")
	@Column(name = "discount_amount")
	private int discountAmount;


	public ComboItems() {
		super();
	}


	public ComboItems(@NotBlank(message = "Combo name cannot be null or empty !!") String comboName, String comboDetail,
			int discountAmount) {
		super();
		this.comboName = comboName;
		this.comboDetail = comboDetail;
		this.discountAmount = discountAmount;
	}


	public String getComboName() {
		return comboName;
	}


	public void setComboName(String comboName) {
		this.comboName = comboName;
	}


	public String getComboDetail() {
		return comboDetail;
	}


	public void setComboDetail(String comboDetail) {
		this.comboDetail = comboDetail;
	}


	public int getDiscountAmount() {
		return discountAmount;
	}


	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}

	
}
