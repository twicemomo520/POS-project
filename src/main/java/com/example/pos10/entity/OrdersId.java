package com.example.pos10.entity;

import java.io.Serializable;

//集中管理 Orders 的複合主鍵
@SuppressWarnings("serial")
public class OrdersId implements Serializable {

	private Integer id;
	
    private String comboName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}
    
    
    
}
