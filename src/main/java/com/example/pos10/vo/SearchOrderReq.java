package com.example.pos10.vo;

import java.time.LocalDate;

public class SearchOrderReq {

	private LocalDate startDate;
	private LocalDate endDate;
	
	public SearchOrderReq() {
		super();
	}

	public SearchOrderReq(LocalDate startDate, LocalDate endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
