package com.example.pos10.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "checkout_list_history")
public class CheckoutListHistory {
	
	@NotBlank(message = "Order Id cannot be null or empty")
	@Id
	@Column(name = "order_id")
	private String orderId;
	
	
	@NotBlank(message = "Table number cannot be null or empty")
	@Column(name = "table_number")
	private String tableNumber;
	

	@NotNull
	@Column(name = "total_price")
	private Integer totalPrice;
	
	
	@NotBlank(message = "Pay type cannot be null or empty")
	@Column(name = "pay_type")
	private String payType;
	
	
	@NotNull
	@Column(name = "checkout")
	private Boolean checkout;
	

	@NotNull(message = "Checkout time cannot be null ")
	@Column(name = "checkout_time")
	private LocalDateTime checkoutTime;
	
	

	public CheckoutListHistory() {
		super();
	}

	public CheckoutListHistory(@NotBlank(message = "Order Id cannot be null or empty") String orderId,
			@NotBlank(message = "Table number cannot be null or empty") String tableNumber,
			@NotBlank(message = "Total price cannot be null or empty") Integer totalPrice,
			@NotBlank(message = "Pay type cannot be null or empty") String payType,
			@NotBlank(message = "Checkout cannot be null or empty") boolean checkout,
			@NotNull(message = "Checkout time cannot be null ") LocalDateTime checkoutTime) {
		super();
		this.orderId = orderId;
		this.tableNumber = tableNumber;
		this.totalPrice = totalPrice;
		this.payType = payType;
		this.checkout = checkout;
		this.checkoutTime = checkoutTime;
	}


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

	public boolean isCheckout() {
		return checkout;
	}

	public void setCheckout(boolean checkout) {
		this.checkout = checkout;
	}

	public LocalDateTime getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
	}


}
