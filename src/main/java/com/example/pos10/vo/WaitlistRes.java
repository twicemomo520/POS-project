package com.example.pos10.vo;

import java.util.List;

public class WaitlistRes {

	private int code;
	private String message;
	private List <WaitlistReq> waitlist; // 用於返回 WaitlistReq 列表

	public WaitlistRes () {
		super ();
	}

	public WaitlistRes (int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	// 用於 WaitlistReq 列表的構造方法
    public WaitlistRes(int code, String message, List <WaitlistReq> waitlist) {
        this.code = code;
        this.message = message;
        this.waitlist = waitlist;
    }

	public int getCode () {
		return code;
	}

	public void setCode (int code) {
		this.code = code;
	}

	public String getMessage () {
		return message;
	}

	public void setMessage (String message) {
		this.message = message;
	}

	public List <WaitlistReq> getWaitlist () {
		return waitlist;
	}

	public void setWaitlist (List <WaitlistReq> waitlist) {
		this.waitlist = waitlist;
	}
}
