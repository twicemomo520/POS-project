package com.example.pos10.vo;

import java.time.LocalTime;

import com.example.pos10.entity.BusinessHours;

public class BusinessHoursReq extends BusinessHours {

	public BusinessHoursReq () {
		super ();
	}

	public BusinessHoursReq (String dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
		super(dayOfWeek, openingTime, closingTime);
	}
}
