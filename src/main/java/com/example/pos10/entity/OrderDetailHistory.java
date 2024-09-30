package com.example.pos10.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "order_detail_history")
public class OrderDetailHistory {
	
	@NotBlank(message = "Id cannot be null or empty")
	@Id
	@Column(name = "id")
	private int id;
	
	@NotBlank(message = "Order Id cannot be null or empty")
	@Column(name = "order_id")
	private String orderId;
	
	
	@NotBlank(message = "Combo name cannot be null or empty")
	@Column(name = "combo_name")
	private String comboName;
	
	
	@NotBlank(message = "Meal detail cannot be null or empty")
	@Column(name = "meal_detail")
	private String mealDetail;
	
	
	@NotBlank(message = "Workstation id cannot be null or empty")
	@Column(name = "workstation_id")
	private int workstationId;
	
	
	@Min (value = 0, message = "Quantity must be greater than 0 !!!")
	@Column(name = "quantity")
	private int quantity;
	
	
	@Min (value = 0, message = "Price must be greater than 0 !!!")
	@Column(name = "price")
	private int price;
	
	@NotBlank(message = "Meal status cannot be null or empty")
	@Column (name = "meal_status")
	private String mealStatus;
	
	
	@NotBlank(message = "Table number cannot be null or empty")
	@Column(name = "table_number")
	private String tableNumber;
	
	
	@NotBlank(message = "Order time cannot be null or empty")
	@Column(name = "order_time")
	private LocalDateTime orderTime;
	
	
	@Column(name = "checkout")
	private Boolean checkout;


	public OrderDetailHistory() {
		super();
	}


	public OrderDetailHistory(int id, String orderId, String comboName, String mealDetail, int workstationId, int quantity, //
			int price, String mealStatus, String tableNumber, LocalDateTime orderTime, Boolean checkout) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.comboName = comboName;
		this.mealDetail = mealDetail;
		this.workstationId = workstationId;
		this.quantity = quantity;
		this.price = price;
		this.mealStatus = mealStatus;
		this.tableNumber = tableNumber;
		this.orderTime = orderTime;
		this.checkout = checkout;
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


	public int getWorkstationId() {
		return workstationId;
	}


	public void setWorkstationId(int workstationId) {
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


	

}
