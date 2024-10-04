package com.example.pos10.vo;

public class AnalysisMealVo {
	
	String mealName;
	long mealTotalRevenue;
	long mealTotalOrders;
	
	public AnalysisMealVo() {
		super();
	}
	
	public AnalysisMealVo(String mealName, long mealTotalRevenue, long mealTotalOrders) {
		super();
		this.mealName = mealName;
		this.mealTotalRevenue = mealTotalRevenue;
		this.mealTotalOrders = mealTotalOrders;
	}
	
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	public long getMealTotalRevenue() {
		return mealTotalRevenue;
	}
	public void setMealTotalRevenue(long mealTotalRevenue) {
		this.mealTotalRevenue = mealTotalRevenue;
	}
	public long getMealTotalOrders() {
		return mealTotalOrders;
	}
	public void setMealTotalOrders(long mealTotalOrders) {
		this.mealTotalOrders = mealTotalOrders;
	}
	
	
	
}
