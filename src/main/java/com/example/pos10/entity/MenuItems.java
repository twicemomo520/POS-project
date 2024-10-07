package com.example.pos10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="menu_items")
public class MenuItems {
	
	@Id
	@NotBlank(message="Meal name cannot be null or empty !!")
	@Column(name="meal_name")
	private String mealName;

	@NotNull(message="Category id cannot be null !")
	@Column(name="category_id")
	private int categoryId;

	@NotNull(message="Workstation id cannot be null !")
	@Column(name="workstation_id")
	private int workstationId;
	
	@NotNull(message="Price cannot be null !")
	@Column(name="price")
	private int price;

	@Column(name="available")
	private boolean available;

	@Column(name="picture_name")
	private String pictureName;

	public MenuItems() {
		super();
	}

	public MenuItems(@NotBlank(message = "Meal name cannot be null or empty !!") String mealName,
			@NotNull(message = "Category id cannot be null !") int categoryId,
			@NotNull(message = "Workstation id cannot be null !") int workstationId,
			@NotNull(message = "Price cannot be null !") int price, boolean available, String pictureName) {
		super();
		this.mealName = mealName;
		this.categoryId = categoryId;
		this.workstationId = workstationId;
		this.price = price;
		this.available = available;
		this.pictureName = pictureName;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(int workstationId) {
		this.workstationId = workstationId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

}
