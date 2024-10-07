package com.example.pos10.vo;

public class AnalysisPopularDishes {
	
	private String name;
	private int orders;
	
	public AnalysisPopularDishes() {
		super();
	}

	public AnalysisPopularDishes(String name, int orders) {
		super();
		this.name = name;
		this.orders = orders;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}
	
	
	
}
