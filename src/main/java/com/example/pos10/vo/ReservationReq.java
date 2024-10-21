package com.example.pos10.vo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.ReservationAndTable;

public class ReservationReq extends Reservation {

	private List <String> tableNumbers; // 儲存桌號的屬性

	public ReservationReq () {
		super ();
	}
	
	public ReservationReq (int reservationId, String customerName, String customerPhoneNumber, String customerEmail,
			Gender customerGender, int reservationPeople, LocalDate reservationDate, LocalTime reservationStartTime,
			LocalTime reservationEndTime, List <ReservationAndTable> reservationTables) {
		super (reservationId, customerName, customerPhoneNumber, customerEmail, customerGender, reservationPeople,
				reservationDate, reservationStartTime, reservationEndTime, reservationTables);
	}
	

	public List <String> getTableNumbers () {
		return tableNumbers;
	}

	public void setTableNumbers (List <String> tableNumbers) {
		this.tableNumbers = tableNumbers;
	}
}