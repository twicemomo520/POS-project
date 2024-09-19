package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entiey.OrderDetailHistory;
import com.example.pos10.entiey.OrderHistory;

public class PosStatisticsRes extends BasicRes{
	private List<JoinOrderVo> joinOrderList;
	
	
	public PosStatisticsRes() {
		super();
	}
	
	public PosStatisticsRes(int code, String message) {
		super(code, message);
	}
	
	public PosStatisticsRes(int code, String message, List<JoinOrderVo> joinOrderList) {
		super(code, message);
		this.joinOrderList = joinOrderList;
	}

	public List<JoinOrderVo> getJoinOrderList() {
		return joinOrderList;
	}

	public void setJoinOrderList(List<JoinOrderVo> joinOrderList) {
		this.joinOrderList = joinOrderList;
	}



	
	
}
