package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.Orders;

public class SelectInPreparationRes extends BasicRes{

	 private List<Orders> data;

	public SelectInPreparationRes() {
		super();
	}

	public SelectInPreparationRes(int code, String message) {
		super(code, message);
	
	}

	public SelectInPreparationRes(int code, String message,List<Orders> data) {
		super(code, message);
		this.data = data;
	}

	public List<Orders> getData() {
		return data;
	}

	public void setData(List<Orders> data) {
		this.data = data;
	}
	 
	 
}
