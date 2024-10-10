package com.example.pos10.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "checkout_list")
public class CheckoutList {

	@Id
	@Column(name = "order_id", length = 60, nullable = false)
	private String orderId;

	@Column(name = "table_number", length = 20)
	private String tableNumber;

	@Column(name = "total_price")
	private Integer totalPrice;

	@Column(name = "pay_type", length = 45)
	private String payType;

	@Column(name = "checkout", columnDefinition = "TINYINT", nullable = false)
	private Boolean checkout;

	@Column(name = "checkout_time")
	private LocalDateTime checkoutTime;

	// Getters and Setters
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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

	public CheckoutList() {
		super();
	}

	public CheckoutList(String orderId, String tableNumber, Integer totalPrice, String payType, Boolean checkout,
			LocalDateTime checkoutTime) {
		super();
		this.orderId = orderId;
		this.tableNumber = tableNumber;
		this.totalPrice = totalPrice;
		this.payType = payType;
		this.checkout = checkout;
		this.checkoutTime = checkoutTime;
	}

}
