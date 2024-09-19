package com.example.pos10.entiey;

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
@Table(name = "order_history")
public class OrderHistory {
	
	@NotBlank(message = "Order Id cannot be null or empty")
	@Id
	@Column(name = "order_id")
	private String orderId;
	
	@NotBlank(message = "Table number cannot be null or empty")
	@Column(name = "table_number")
	private String tableNumber;

	@NotBlank(message = "Total price cannot be null or empty")
	@Column(name = "total_price")
	private int totalPrice;
	
	@NotBlank(message = "Pay type cannot be null or empty")
	@Column(name = "pay_type")
	private String payType;
	
	@NotBlank(message = "Checkout cannot be null or empty")
	@Column(name = "checkout")
	private boolean checkout;

	@NotNull(message = "Checkout time cannot be null ")
	@Column(name = "checkout_time")
	private LocalDateTime checkoutTime;
	
	@NotBlank(message = "Member account cannot be null or empty")
	@Column(name = "member_account")
	private String memberAccount;
	
	
	
	
	public OrderHistory() {
		super();
	}

	public OrderHistory(String orderId, String tableNumber, int totalPrice, String payType, boolean checkout, LocalDateTime checkoutTime, String memberAccount) {
		super();
		this.orderId = orderId;
		this.tableNumber = tableNumber;
		this.totalPrice = totalPrice;
		this.payType = payType;
		this.checkout = checkout;
		this.checkoutTime = checkoutTime;
		this.memberAccount = memberAccount;
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

	public String getMemberAccount() {
		return memberAccount;
	}

	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}

	
	

}
