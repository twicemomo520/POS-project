package com.example.pos10.vo;

import java.util.List;

public class AnalysisVo {
	
	private long totalRevenue;
	private long totalOrders;
	private List<AnalysisPopularDishes>popularDishes;
	private List<AnalysisRevenueGrowth>revenueGrowth;
	private AnalysisMealVo analysisMealVo;
	
	public AnalysisVo() {
		super();
	}

	public AnalysisVo(long totalRevenue, long totalOrders, List<AnalysisPopularDishes> popularDishes,
			List<AnalysisRevenueGrowth> revenueGrowth, AnalysisMealVo analysisMealVo) {
		super();
		this.totalRevenue = totalRevenue;
		this.totalOrders = totalOrders;
		this.popularDishes = popularDishes;
		this.revenueGrowth = revenueGrowth;
		this.analysisMealVo = analysisMealVo;
	}

	public long getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(long totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public List<AnalysisPopularDishes> getPopularDishes() {
		return popularDishes;
	}

	public void setPopularDishes(List<AnalysisPopularDishes> popularDishes) {
		this.popularDishes = popularDishes;
	}

	public List<AnalysisRevenueGrowth> getRevenueGrowth() {
		return revenueGrowth;
	}

	public void setRevenueGrowth(List<AnalysisRevenueGrowth> revenueGrowth) {
		this.revenueGrowth = revenueGrowth;
	}

	public AnalysisMealVo getAnalysisMealVo() {
		return analysisMealVo;
	}

	public void setAnalysisMealVo(AnalysisMealVo analysisMealVo) {
		this.analysisMealVo = analysisMealVo;
	}

	
}
