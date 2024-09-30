package com.example.pos10.vo;

import com.example.pos10.entity.Staff;

public class StaffInfoRes extends BasicRes {

	private Staff staff;

	public StaffInfoRes() {
		super();
	}

	public StaffInfoRes(int code, String message, Staff staff) {
		super(code, message);
		this.staff = staff;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

}
