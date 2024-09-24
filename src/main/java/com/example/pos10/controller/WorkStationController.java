package com.example.pos10.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.WorkstationService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.SearchWorkRes;
import com.example.pos10.vo.WorkStationDeleteReq;
import com.example.pos10.vo.WorkStationReq;

@RestController
@CrossOrigin
public class WorkStationController {

	@Autowired
	private WorkstationService workstationService;

	@PostMapping(value = "workstation/createworkstation")
	public BasicRes createworkstation(@Valid @RequestBody WorkStationReq req) {
		return workstationService.workCreate(req);
	}
	
	@PostMapping(value = "workstation/updateworkstation")
	public BasicRes updateworkstation(@Valid @RequestBody WorkStationReq req) {
		return workstationService.workUpdate(req);
	}
	
	@PostMapping(value = "workstation/deleteworkstation")
	public BasicRes deleteworkstation(@Valid @RequestBody WorkStationDeleteReq req) {
		return workstationService.workDelete(req);
	}
	
	@PostMapping(value = "workstation/searchworkstation")
	public SearchWorkRes workSearch(@Valid @RequestBody WorkStationDeleteReq req) {
		return workstationService.workSearch(req);
	}
}
