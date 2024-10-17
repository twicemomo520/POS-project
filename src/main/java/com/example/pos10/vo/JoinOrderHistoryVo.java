package com.example.pos10.vo;

import java.time.LocalDateTime;

public class JoinOrderHistoryVo {

    private int id;
    private String orderId;
    private String orderMealId;
    private String comboName;
    private String mealName;
    private String options;
    private Integer workstationId;
    private int price;
    private String mealStatus;
    private String tableNumber;
    private LocalDateTime orderTime;
    private Boolean checkout;
    private LocalDateTime checkoutTime;

    
    public JoinOrderHistoryVo() {
        super();
    }


	public JoinOrderHistoryVo(int id, String orderId, String orderMealId, String comboName, String mealName,
			String options, Integer workstationId, int price, String mealStatus, String tableNumber,
			LocalDateTime orderTime, Boolean checkout, LocalDateTime checkoutTime) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.orderMealId = orderMealId;
		this.comboName = comboName;
		this.mealName = mealName;
		this.options = options;
		this.workstationId = workstationId;
		this.price = price;
		this.mealStatus = mealStatus;
		this.tableNumber = tableNumber;
		this.orderTime = orderTime;
		this.checkout = checkout;
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


	public Integer getWorkstationId() {
		return workstationId;
	}


	public void setWorkstationId(Integer workstationId) {
		this.workstationId = workstationId;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public String getMealStatus() {
		return mealStatus;
	}


	public void setMealStatus(String mealStatus) {
		this.mealStatus = mealStatus;
	}


	public String getTableNumber() {
		return tableNumber;
	}


	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}


	public LocalDateTime getOrderTime() {
		return orderTime;
	}


	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}


	public Boolean getCheckout() {
		return checkout;
	}


	public void setCheckout(Boolean checkout) {
		this.checkout = checkout;
	}


	public LocalDateTime getCheckoutTime() {
		return checkoutTime;
	}


	public void setCheckoutTime(LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
	}




    
}