package com.example.pos10.vo;

import java.time.LocalTime;

import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.ReservationManagement;

public class ReservationReq extends Reservation {

	public ReservationReq () {
		super ();
	}

	public ReservationReq (int reservationId, String customerName, String customerPhoneNumber, String customerEmail,
			Gender customerGender, int reservationPeople, LocalTime reservationTime, ReservationManagement reservationManagement) {
		super (reservationId, customerName, customerPhoneNumber, customerEmail, customerGender, reservationPeople,
				reservationTime, reservationManagement);
	}

}
