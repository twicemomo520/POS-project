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
@Table(name = "orders")
public class Orders {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@NotNull(message = "Id cannot be null!")
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

    @Column(name = "meal_status", columnDefinition = "ENUM('準備中', '待送餐點', '已送達')")
    private String mealStatus;

    @Column(name = "table_number")
    private String tableNumber;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "checkout")
    private Boolean checkout = false;

	public Orders() {
		super();
	}

	public Orders(Integer id, String orderId, String orderMealId,String comboName, String mealName,
			String options, Integer workstationId, Integer price, String mealStatus, String tableNumber,
			LocalDateTime orderTime, Boolean checkout) {
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

	public String getOrderId() {
		return orderId;
	}

	public String getOrderMealId() {
		return orderMealId;
	}

	public String getComboName() {
		return comboName;
	}

	public String getMealName() {
		return mealName;
	}

	public String getOptions() {
		return options;
	}

	public Integer getWorkstationId() {
		return workstationId;
	}

	public Integer getPrice() {
		return price;
	}

	public String getMealStatus() {
		return mealStatus;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public Boolean getCheckout() {
		return checkout;
	}
    
}
