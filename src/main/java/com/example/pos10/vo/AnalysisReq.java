package com.example.pos10.vo;

import java.time.LocalDate;

public class AnalysisReq {
	
	private LocalDate startDate;
	private LocalDate endDate;
	private String mealName;
	
	public AnalysisReq() {
		super();

	}
	
	public AnalysisReq(LocalDate startDate, LocalDate endDate, String mealName) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.mealName = mealName;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	
	

}
