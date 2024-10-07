package com.example.pos10.vo;

import java.time.LocalDateTime;

public class JoinOrderHistoryVo {

    private int id;
    private String orderId;
    private String orderMealId;
    private String comboName;
    private String mealDetail;
    private Integer workstationId;
    private int quantity;
    private int price;
    private String mealStatus;
    private String tableNumber;
    private LocalDateTime orderTime;
    private Boolean checkout;

    private int totalPrice;
    private String payType;
    private LocalDateTime checkoutTime;
    private String memberAccount;
    
//    private long totalRevenue; // 改成 long
//    private long totalOrders;  // 改成 long
    
    public JoinOrderHistoryVo() {
        super();
    }

public JoinOrderHistoryVo(int id, String orderId, String orderMealId, String comboName, String mealDetail,
		Integer workstationId, int quantity, int price, String mealStatus, String tableNumber, LocalDateTime orderTime,
		Boolean checkout, int totalPrice, String payType, LocalDateTime checkoutTime, String memberAccount) {
	super();
	this.id = id;
	this.orderId = orderId;
	this.orderMealId = orderMealId;
	this.comboName = comboName;
	this.mealDetail = mealDetail;
	this.workstationId = workstationId;
	this.quantity = quantity;
	this.price = price;
	this.mealStatus = mealStatus;
	this.tableNumber = tableNumber;
	this.orderTime = orderTime;
	this.checkout = checkout;
	this.totalPrice = totalPrice;
	this.payType = payType;
	this.checkoutTime = checkoutTime;
	this.memberAccount = memberAccount;
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

public String getMealDetail() {
	return mealDetail;
}

public void setMealDetail(String mealDetail) {
	this.mealDetail = mealDetail;
}

public Integer getWorkstationId() {
	return workstationId;
}

public void setWorkstationId(Integer workstationId) {
	this.workstationId = workstationId;
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

public int getTotalPrice() {
	return totalPrice;
}

public void setTotalPrice(int totalPrice) {
	this.totalPrice = totalPrice;
}

public String getPayType() {
	return payType;
}

public void setPayType(String payType) {
	this.payType = payType;
}

public LocalDateTime getCheckoutTime() {
	return checkoutTime;
}

public void setCheckoutTime(LocalDateTime checkoutTime) {
	this.checkoutTime = checkoutTime;
}

public String getMemberAccount() {
	return memberAccount;
}

public void setMemberAccount(String memberAccount) {
	this.memberAccount = memberAccount;
}

    
}