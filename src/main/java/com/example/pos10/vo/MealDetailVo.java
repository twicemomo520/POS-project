package com.example.pos10.vo;

import java.util.List;

public class MealDetailVo {
	private String mealName;
	private List<String>options;
	
	public MealDetailVo() {
		super();
	}
	public MealDetailVo(String mealName, List<String> options) {
		super();
		this.mealName = mealName;
		this.options = options;
	}
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
}
