package com.example.POS.project.vo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.POS.project.entity.ReservationManagement;
import com.example.POS.project.entity.TableManagement;

public class ReservationManagementReq extends ReservationManagement{

	public ReservationManagementReq () {
		super ();
	}

	public ReservationManagementReq (int indexId, TableManagement tableManagement, LocalDate reservationDate, LocalTime reservationStarttime) {
		super (indexId, tableManagement, reservationDate, reservationStarttime);
	}
}
