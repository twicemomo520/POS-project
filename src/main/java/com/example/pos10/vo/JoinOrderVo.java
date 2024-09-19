package com.example.pos10.vo;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

public class JoinOrderVo {

	private int id;
	@NotBlank(message = "cannot be null or empty")
	private String orderId;
	@NotBlank(message = "cannot be null or empty")
	private String comboName;
	@NotBlank(message = "cannot be null or empty")
	private String mealDetail;

	private int quantity;

	private int price;

	private LocalDateTime checkoutTime;
	
	
	public JoinOrderVo() {
		super();

	}


	public JoinOrderVo(int id, String orderId, String comboName, String mealDetail, int quantity, int price,
			LocalDateTime checkoutTime) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.comboName = comboName;
		this.mealDetail = mealDetail;
		this.quantity = quantity;
		this.price = price;
		this.checkoutTime = checkoutTime;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getComboName() {
		return comboName;
	}


	public void setComboName(String comboName) {
		this.comboName = comboName;
	}


	public String getMealDetail() {
		return mealDetail;
	}


	public void setMealDetail(String mealDetail) {
		this.mealDetail = mealDetail;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public LocalDateTime getCheckoutTime() {
		return checkoutTime;
	}


	public void setCheckoutTime(LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
	}
	

	
}
