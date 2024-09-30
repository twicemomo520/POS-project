package com.example.pos10.controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.MemberService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ForgotPasswordReq;
import com.example.pos10.vo.LoginMemberReq;
import com.example.pos10.vo.RegisterMemberReq;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@PostMapping("/register")
	public BasicRes registerMember(@RequestBody RegisterMemberReq req) {
		return memberService.registerMember(req);
	}

	@PostMapping("/checklogin")
	public BasicRes loginMember(@RequestBody LoginMemberReq req) {
		return memberService.loginMember(req);
	}
	
	@GetMapping("/{memberId}")
	public BasicRes getMemberById(@PathVariable int memberId) {
	    return memberService.getMemberInfo(memberId);
	}

	@PostMapping("/forgotpassword")
	public BasicRes forgotPassword(@RequestBody ForgotPasswordReq req) {
		return memberService.forgotPassword(req);
	}
	
	//LoginMemberReq格式一樣
	@PostMapping("/resetpassword")
	public BasicRes resetPassword(@RequestBody LoginMemberReq req) {
		return memberService.resetPassword(req);
	}
	
	
	
	
}