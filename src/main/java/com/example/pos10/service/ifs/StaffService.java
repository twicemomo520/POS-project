package com.example.pos10.service.ifs;

import com.example.pos10.vo.AllStaffInfoRes;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.LoginStaffReq;
import com.example.pos10.vo.RegisterStaffReq;
import com.example.pos10.vo.StaffForgotPasswordReq;
import com.example.pos10.vo.UpdateStaffReq;

public interface StaffService  {
	
	public BasicRes registerStaff(RegisterStaffReq req);
	
	public BasicRes updateStaff(UpdateStaffReq req);
	
	public AllStaffInfoRes searchAllstaff();
	
	public BasicRes deleteStaff(String staffNumber);
	
	public BasicRes loginStaff(LoginStaffReq req);
	
	public BasicRes getStaffInfo(String  staffNumber);
	
	public BasicRes resetPassword(LoginStaffReq req);
	
	public BasicRes forgotPassword(StaffForgotPasswordReq req);
	
	public BasicRes updateFirstLogin(String  staffNumber);
	
}
