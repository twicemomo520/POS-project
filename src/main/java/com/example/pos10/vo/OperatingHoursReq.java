package com.example.pos10.vo;

import java.time.LocalTime;

import com.example.pos10.entity.OperatingHours;

public class OperatingHoursReq extends OperatingHours {

	public OperatingHoursReq() {
	}

	public OperatingHoursReq (DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime, int diningDuration) {
		super (dayOfWeek, openingTime, closingTime, diningDuration);
	}
}