package com.example.pos10.vo;

import java.util.List;

public class ComboVo {
	
	private int categoryId;
	
    private String comboName;
    
    private List<ComboDetailVo> comboDetail;
    
    private int discountAmount;

	public ComboVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComboVo(int categoryId, String comboName, List<ComboDetailVo> comboDetail, int discountAmount) {
		super();
		this.categoryId = categoryId;
		this.comboName = comboName;
		this.comboDetail = comboDetail;
		this.discountAmount = discountAmount;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}

	public List<ComboDetailVo> getComboDetail() {
		return comboDetail;
	}

	public void setComboDetail(List<ComboDetailVo> comboDetail) {
		this.comboDetail = comboDetail;
	}

	public int getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}
	
    
}
