package com.example.pos10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="categories")
public class Categories {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="category_id")
	private int categoryId;
	
	@Column(name="category")
	private String category;
	
	@Column(name = "workstation_id")
	private Integer workstationId;

	public Categories() {
		super();
	}
	
	public Categories(int categoryId, String category, int workstationId) {
		super();
		this.categoryId = categoryId;
		this.category = category;
		this.workstationId = workstationId;
	}

	public Categories(int categoryId, String category) {
		super();
		this.categoryId = categoryId;
		this.category = category;
	}

	public Categories(String category) {
		super();
		this.category = category;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(Integer workstationId) {
		this.workstationId = workstationId;
	}

	
}
