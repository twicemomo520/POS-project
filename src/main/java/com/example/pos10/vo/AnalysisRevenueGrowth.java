package com.example.pos10.vo;

import java.time.LocalDate;

public class AnalysisRevenueGrowth {
	
	private LocalDate day;
	private int revenue ;
	
	public AnalysisRevenueGrowth() {
		super();
	}

	public AnalysisRevenueGrowth(LocalDate day, int revenue) {
		super();
		this.day = day;
		this.revenue = revenue;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public int getRevenue() {
		return revenue;
	}

	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}

	
	
	
	
}
