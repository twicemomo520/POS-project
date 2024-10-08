package com.example.pos10.vo;

import java.time.LocalDateTime;
import java.util.List;

public class MealVo {
	private String orderMealId;
	private String comboName;
	private List<MealDetailVo> mealDetail;
	private LocalDateTime orderTime;
	
	public MealVo() {
		super();
	}

	public MealVo(String orderMealId, String comboName, List<MealDetailVo> mealDetail, LocalDateTime orderTime) {
		super();
		this.orderMealId = orderMealId;
		this.comboName = comboName;
		this.mealDetail = mealDetail;
		this.orderTime = orderTime;
	}

	public String getOrderMealId() {
		return orderMealId;
	}

	public void setOrderMealId(String orderMealId) {
		this.orderMealId = orderMealId;
	}

	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}

	public List<MealDetailVo> getMealDetail() {
		return mealDetail;
	}

	public void setMealDetail(List<MealDetailVo> mealDetail) {
		this.mealDetail = mealDetail;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}
}
