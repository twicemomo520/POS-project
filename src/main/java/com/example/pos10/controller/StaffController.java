package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.StaffService;
import com.example.pos10.vo.AllStaffInfoRes;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.LoginStaffReq;
import com.example.pos10.vo.RegisterStaffReq;
import com.example.pos10.vo.StaffForgotPasswordReq;
import com.example.pos10.vo.UpdateStaffReq;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

	@Autowired
	private StaffService staffService;
	
	//回傳全部員工
	@GetMapping("/all")
	public AllStaffInfoRes searchAllstaff() {
		return staffService.searchAllstaff();
	}
	
	
	//新增員工
	@PostMapping("/register")
	public BasicRes registerStaff(@RequestBody RegisterStaffReq req) {
		return staffService.registerStaff(req);
	}
	
	//修改員工資料
	@PostMapping("/updateInfo")
	public BasicRes updateStaff(@RequestBody UpdateStaffReq req) {
		return staffService.updateStaff(req);
	}
	
	@PostMapping("/delete")
	public BasicRes deleteStaff(@RequestBody String staffNumber) {
		staffNumber = staffNumber.trim();  // 使用 trim() 方法去掉前後的空白
	    return staffService.deleteStaff(staffNumber);
	}
	
	@PostMapping("/checklogin")
	public BasicRes loginStaff(@RequestBody LoginStaffReq req) {
		return staffService.loginStaff(req);
	}
	
	@GetMapping("/{staffNumber}")
	public BasicRes getStaffInfo(@PathVariable String staffNumber) {
	    return staffService.getStaffInfo(staffNumber);
	}
	

	@PostMapping("/resetPassword")
	public BasicRes resetPassword(@RequestBody LoginStaffReq req) {
		return staffService.resetPassword(req);
	}
	
	@PostMapping("/forgotpassword")
	public BasicRes forgotPassword(@RequestBody StaffForgotPasswordReq req) {
		return staffService.forgotPassword(req);
	}
	
	@GetMapping("/updateFirstLogin/{staffNumber}")
	public BasicRes updateFirstLogin(@PathVariable String staffNumber) {
	    return staffService.updateFirstLogin(staffNumber);
	}
	
}
