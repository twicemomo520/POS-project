package com.example.pos10.service.ifs;

import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ForgotPasswordReq;
import com.example.pos10.vo.LoginMemberReq;
import com.example.pos10.vo.RegisterMemberReq;

public interface MemberService {
	
	public BasicRes registerMember(RegisterMemberReq req);
	
	public BasicRes loginMember(LoginMemberReq req);
	
	public BasicRes getMemberInfo(int memberId);
	
	public BasicRes forgotPassword(ForgotPasswordReq req);
	
	public BasicRes resetPassword(LoginMemberReq req);
}
