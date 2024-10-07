package com.example.pos10.vo;

public class CheckoutDetailRes {

	private String orderId;
	private String orderMealId;
	private String comboName;
	private String mealName;
	private String options;
	private Integer price;
	private String tableNumber;
	private Boolean checkout;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public Boolean getCheckout() {
		return checkout;
	}

	public void setCheckout(Boolean checkout) {
		this.checkout = checkout;
	}

}
