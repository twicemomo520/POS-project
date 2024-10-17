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
@Table(name = "orders_history")
public class OrdersHistory {
	
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "order_meal_id")
	private String orderMealId;
	
    @Column(name = "combo_name")
    private String comboName;
	
	
	@Column(name = "meal_name")
	private String mealName;
	
    @Column(name = "options")
    private String options;
	
	@Column(name = "workstation_id")
	private Integer workstationId;

	
	@Column(name = "price")
	private Integer price;
	
	@Column (name = "meal_status")
	private String mealStatus;
	
	
	@Column(name = "table_number")
	private String tableNumber;
	
	
	@Column(name = "order_time")
	private LocalDateTime orderTime;
	
	
	@Column(name = "checkout")
	private Boolean checkout = false;


	public OrdersHistory() {
		super();
	}


	public OrdersHistory(@NotBlank(message = "Id cannot be null or empty") Integer id,
			@NotBlank(message = "Order Id cannot be null or empty") String orderId,
			@NotBlank(message = "Order meal Id cannot be null or empty") String orderMealId,
			@NotBlank(message = "Combo name cannot be null or empty") String comboName,
			@NotBlank(message = "Combo name cannot be null or empty") String mealName,
			@NotBlank(message = "Options cannot be null or empty") String options,
			@NotBlank(message = "Workstation id cannot be null or empty") Integer workstationId,
			@Min(value = 0, message = "Price must be greater than 0 !!!") Integer price,
			@NotBlank(message = "Meal status cannot be null or empty") String mealStatus,
			@NotBlank(message = "Table number cannot be null or empty") String tableNumber,
			@NotBlank(message = "Order time cannot be null or empty") LocalDateTime orderTime, Boolean checkout) {
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
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
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


	public Integer getPrice() {
		return price;
	}


	public void setPrice(Integer price) {
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
