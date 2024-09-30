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

	@Column(name="meal_description")
	private String mealDescription;

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

	@Column(name="featured")
	private boolean featured;

	@Column(name="picture_name")
	private String pictureName;

	public MenuItems() {
		super();
	}

	public MenuItems(String mealName, String mealDescription, int categoryId, int workstationId, int price) {
		super();
		this.mealName = mealName;
		this.mealDescription = mealDescription;
		this.categoryId = categoryId;
		this.workstationId = workstationId;
		this.price = price;
	}
	
	public MenuItems(String mealName, String mealDescription, int categoryId, int workstationId, int price,
			boolean available, boolean featured, String pictureName) {
		super();
		this.mealName = mealName;
		this.mealDescription = mealDescription;
		this.categoryId = categoryId;
		this.workstationId = workstationId;
		this.price = price;
		this.available = available;
		this.featured = featured;
		this.pictureName = pictureName;
	}

	public String getMealName() {
		return mealName;
	}

	public String getMealDescription() {
		return mealDescription;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public int getWorkstationId() {
		return workstationId;
	}

	public int getPrice() {
		return price;
	}

	public boolean isAvailable() {
		return available;
	}

	public boolean isFeatured() {
		return featured;
	}

	public String getPictureName() {
		return pictureName;
	}
	
}
