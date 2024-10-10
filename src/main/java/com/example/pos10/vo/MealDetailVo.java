package com.example.pos10.vo;

import java.util.List;

public class MealDetailVo {
	
	private int id;
	private String mealName;
	private List<String>options;
	
	public MealDetailVo() {
		super();

	}
	public MealDetailVo(int id, String mealName, List<String> options) {
		super();
		this.id = id;
		this.mealName = mealName;
		this.options = options;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
