package com.example.pos10.vo;

import com.example.pos10.entity.Member;

public class MemberInfoRes extends BasicRes {
	
	private Member member;

	public MemberInfoRes() {
		super();
	}

	public MemberInfoRes(int code, String message, Member member) {
		super(code, message);
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
}
