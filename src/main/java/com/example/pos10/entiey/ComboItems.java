package com.example.pos10.entiey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "combo_items")
public class ComboItems {
	
	@Id
	@NotBlank(message="Combo name cannot be null or empty !!")
	@Column(name = "combo_name")
	private String comboName;

	@Column(name = "combo_detail")
	private String comboDetail;

	@Column(name = "discount_amount")
	private int discountAmount;

	@Column(name = "combo_description")
	private String comDescription;
	
}
