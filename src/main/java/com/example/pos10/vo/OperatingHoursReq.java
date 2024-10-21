package com.example.pos10.vo;

import java.time.LocalTime;

import com.example.pos10.entity.OperatingHours;

public class OperatingHoursReq extends OperatingHours {

	public OperatingHoursReq () {
		super ();
	}

	public OperatingHoursReq (int id, DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime,
			int diningDuration, Integer cleaningBreak) {
		super (id, dayOfWeek, openingTime, closingTime, diningDuration, cleaningBreak);
	}	
}