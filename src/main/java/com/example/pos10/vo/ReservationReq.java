package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.ReservationManagement;
import com.example.pos10.entity.TableManagement;

public class ReservationReq extends Reservation {

	public ReservationReq () {
		super ();
	}

	public ReservationReq (int reservationId, String customerName, String customerPhoneNumber, String customerEmail, Gender customerGender, 
			int reservationPeople, ReservationManagement reservationManagement, List <TableManagement> tables) {
		super (reservationId, customerName, customerPhoneNumber, customerEmail, customerGender, reservationPeople,
				reservationManagement, tables);
	}
}
