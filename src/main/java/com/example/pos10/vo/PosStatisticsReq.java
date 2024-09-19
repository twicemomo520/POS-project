package com.example.pos10.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PosStatisticsReq {
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	
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
