package com.example.pos10.vo;

import java.util.List;

public class ReservationRes {

    private int code;
    private String message;
    private List <ReservationReq> reservations;  // 添加這個欄位
    
	public ReservationRes () {
		super ();
	}

	public ReservationRes (int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ReservationRes(int code, String message, List <ReservationReq> reservations) {
		super();
		this.code = code;
		this.message = message;
		this.reservations = reservations;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ReservationReq> getReservations() {
		return reservations;
	}

	public void setReservations(List<ReservationReq> reservations) {
		this.reservations = reservations;
	}
}
