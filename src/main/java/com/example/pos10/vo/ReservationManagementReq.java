package com.example.pos10.vo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.pos10.entity.ReservationManagement;
import com.example.pos10.entity.TableManagement;

public class ReservationManagementReq extends ReservationManagement{

	public ReservationManagementReq () {
		super ();
	}

	public ReservationManagementReq (int indexId, TableManagement tableManagement, LocalDate reservationDate, LocalTime reservationStarttime) {
		super (indexId, tableManagement, reservationDate, reservationStarttime);
	}
}
