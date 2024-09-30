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

	public Categories() {
		super();
	}

	public Categories(String category) {
		super();
		this.category = category;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getCategory() {
		return category;
	}
	
}
