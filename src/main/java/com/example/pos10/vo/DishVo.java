package com.example.pos10.vo;

public class DishVo {

	private String dishName;

	private int price;

	public DishVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DishVo(String dishName, int price) {
		super();
		this.dishName = dishName;
		this.price = price;
	}

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
