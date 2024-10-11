package com.example.pos10.vo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;

public class ReservationReq extends Reservation {

	private List<String> tableNumbers; // 儲存桌號的屬性

	public ReservationReq() {
		super();
	}

	public ReservationReq(int reservationId, String customerName, String customerPhoneNumber, String customerEmail,
			Gender customerGender, int reservationPeople, LocalDate reservationDate, LocalTime reservationStartTime,
			LocalTime reservationEndingTime, List<TableManagement> tables) {
		super(reservationId, customerName, customerPhoneNumber, customerEmail, customerGender, reservationPeople,
				reservationDate, reservationStartTime, reservationEndingTime, tables);

		// 提取桌號並保存到 tableNumbers
		this.tableNumbers = tables.stream().map(TableManagement::getTableNumber) // 確保有 getTableNumber 方法
				.collect(Collectors.toList());
	}

	public List<String> getTableNumbers() {
		return tableNumbers;
	}

	public void setTableNumbers(List<String> tableNumbers) {
		this.tableNumbers = tableNumbers;
	}

}
