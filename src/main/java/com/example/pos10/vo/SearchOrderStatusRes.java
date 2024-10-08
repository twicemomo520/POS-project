package com.example.pos10.vo;

import java.util.List;

public class SearchOrderStatusRes extends BasicRes{
	
	private List<SearchOrderStatusVo> preparingOrders;
	
	private List<SearchOrderStatusVo> undeliveredOrders;
	
	private List<SearchOrderStatusVo> deliveredOrders;

	public SearchOrderStatusRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SearchOrderStatusRes(int code, String message, List<SearchOrderStatusVo> preparingOrders, //
			List<SearchOrderStatusVo> undeliveredOrders, List<SearchOrderStatusVo> deliveredOrders) {
		super(code, message);
		this.preparingOrders = preparingOrders;
		this.undeliveredOrders = undeliveredOrders;
		this.deliveredOrders = deliveredOrders;
	}

	public List<SearchOrderStatusVo> getPreparingOrders() {
		return preparingOrders;
	}

	public void setPreparingOrders(List<SearchOrderStatusVo> preparingOrders) {
		this.preparingOrders = preparingOrders;
	}

	public List<SearchOrderStatusVo> getUndeliveredOrders() {
		return undeliveredOrders;
	}

	public void setUndeliveredOrders(List<SearchOrderStatusVo> undeliveredOrders) {
		this.undeliveredOrders = undeliveredOrders;
	}

	public List<SearchOrderStatusVo> getDeliveredOrders() {
		return deliveredOrders;
	}

	public void setDeliveredOrders(List<SearchOrderStatusVo> deliveredOrders) {
		this.deliveredOrders = deliveredOrders;
	}

}
