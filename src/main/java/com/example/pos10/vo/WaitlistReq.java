package com.example.pos10.vo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.pos10.entity.Waitlist;

public class WaitlistReq extends Waitlist{

	public WaitlistReq () {
		super ();
	}

	public WaitlistReq (int waitlistId, String customerName, String customerPhoneNumber, String customerEmail,
			Gender customerGender, int waitListPeople, LocalDate waitingDate, LocalTime waitTime, int waitlistOrder) {
		super (waitlistId, customerName, customerPhoneNumber, customerEmail, customerGender, waitListPeople, waitingDate,
				waitTime, waitlistOrder);
	}
}
