package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.OrdersHistory;
import com.example.pos10.entity.CheckoutListHistory;

public class PosStatisticsRes extends BasicRes{
	private List<JoinOrderHistoryVo> joinOrderList;
	
	
	public PosStatisticsRes() {
		super();
	}
	
	public PosStatisticsRes(int code, String message) {
		super(code, message);
	}
	
	public PosStatisticsRes(int code, String message, List<JoinOrderHistoryVo> joinOrderList) {
		super(code, message);
		this.joinOrderList = joinOrderList;
	}

	public List<JoinOrderHistoryVo> getJoinOrderList() {
		return joinOrderList;
	}

	public void setJoinOrderList(List<JoinOrderHistoryVo> joinOrderList) {
		this.joinOrderList = joinOrderList;
	}



	
	
}
