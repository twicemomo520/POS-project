package com.example.pos10.vo;

public class LoginMemberRes extends BasicRes {
	
	private int  memberId;

	public LoginMemberRes() {
		super();
	}

	public LoginMemberRes(int code, String message, int memberId) {
		super(code, message); 
		this.memberId = memberId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(int  memberId) {
		this.memberId = memberId;
	}

	
	
}