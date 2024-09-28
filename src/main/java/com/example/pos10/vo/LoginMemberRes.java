package com.example.pos10.vo;

public class LoginMemberRes extends BasicRes {
	
	private int  memberId;

	public LoginMemberRes() {
		super();
	}

	// 帶有 memberId 的建構子
	public LoginMemberRes(int code, String message, int memberId) {
		super(code, message); // 使用父類別的建構子設置 code 和 message
		this.memberId = memberId;
	}

	// 取得 memberId
	public Integer getMemberId() {
		return memberId;
	}

	// 設置 memberId
	public void setMemberId(int  memberId) {
		this.memberId = memberId;
	}

	
	
}