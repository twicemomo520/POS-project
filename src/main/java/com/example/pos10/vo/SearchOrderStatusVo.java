package com.example.pos10.vo;

import java.util.List;

public class SearchOrderStatusVo {
	private String tableNumber;
	private String orderId;
	private List<MealVo>mealList;
	
	public SearchOrderStatusVo() {
		super();
	}

	public SearchOrderStatusVo(String tableNumber, String orderId, List<MealVo> mealList) {
		super();
		this.tableNumber = tableNumber;
		this.orderId = orderId;
		this.mealList = mealList;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<MealVo> getMealList() {
		return mealList;
	}

	public void setMealList(List<MealVo> mealList) {
		this.mealList = mealList;
	}
}
