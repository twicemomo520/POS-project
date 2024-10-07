package com.example.POS.project.vo;

import java.time.LocalTime;

import com.example.POS.project.entity.BusinessHours;

public class BusinessHoursReq extends BusinessHours {

	public BusinessHoursReq () {
		super ();
	}

	public BusinessHoursReq (String dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
		super(dayOfWeek, openingTime, closingTime);
	}
}
