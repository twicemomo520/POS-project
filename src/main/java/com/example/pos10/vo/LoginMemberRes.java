package com.example.pos10.vo;

public class LoginMemberRes extends BasicRes {
	
	private int  memberId;

	public LoginMemberRes() {
		super();
	}

	// �a�� memberId ���غc�l
	public LoginMemberRes(int code, String message, int memberId) {
		super(code, message); // �ϥΤ����O���غc�l�]�m code �M message
		this.memberId = memberId;
	}

	// ���o memberId
	public Integer getMemberId() {
		return memberId;
	}

	// �]�m memberId
	public void setMemberId(int  memberId) {
		this.memberId = memberId;
	}

	
	
}