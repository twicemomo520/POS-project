package com.example.pos10.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.example.pos10.entity.Orders;

public class AddOrderReq extends Orders{
	
	private List<Orders> ordersList;

	public List<Orders> getOrdersList() {
		return ordersList;
	}

	public void setOrdersList(List<Orders> ordersList) {
		this.ordersList = ordersList;
	}
	

}
