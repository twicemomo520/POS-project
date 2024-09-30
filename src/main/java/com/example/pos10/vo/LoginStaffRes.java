package com.example.pos10.vo;

public class LoginStaffRes extends BasicRes {
	
	private String  staffNumber;

	public LoginStaffRes() {
		super();
	}

	// 帶有 memberId 的建構子
	public LoginStaffRes(int code, String message, String staffNumber) {
		super(code, message); // 使用父類別的建構子設置 code 和 message
		this.staffNumber = staffNumber;
	}

	public String getStaffNumber() {
		return staffNumber;
	}

	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}


}
