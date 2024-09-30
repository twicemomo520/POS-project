package com.example.pos10.vo;

public class LoginStaffRes extends BasicRes {
	
	private String  staffNumber;

	public LoginStaffRes() {
		super();
	}

	// �a�� memberId ���غc�l
	public LoginStaffRes(int code, String message, String staffNumber) {
		super(code, message); // �ϥΤ����O���غc�l�]�m code �M message
		this.staffNumber = staffNumber;
	}

	public String getStaffNumber() {
		return staffNumber;
	}

	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}


}
