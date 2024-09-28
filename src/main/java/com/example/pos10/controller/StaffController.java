package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.StaffService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.RegisterStaffReq;
import com.example.pos10.vo.UpdateStaffReq;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

	@Autowired
	private StaffService staffservice;
	
	@PostMapping("/register")
	public BasicRes registerStaff(@RequestBody RegisterStaffReq req) {
		return staffservice.registerStaff(req);
	}
	
	
	@PostMapping("/update")
	public BasicRes updateStaff(@RequestBody UpdateStaffReq req) {
		return staffservice.updateStaff(req);
	}
	
	
	
	
	
	
	
}
