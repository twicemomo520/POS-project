package com.example.pos10.vo;

public class CheckLoginRes {
	
	private int memberId;
	private String pwd;
	

	public CheckLoginRes() {
		super();
	}
	public CheckLoginRes(int memberId, String pwd) {
		super();
		this.memberId = memberId;
		this.pwd = pwd;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
}
