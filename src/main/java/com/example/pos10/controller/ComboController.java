package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.ComboService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.DeleteCbReq;
import com.example.pos10.vo.SearchCbReq;
import com.example.pos10.vo.UpdateCbReq;


@RestController
@CrossOrigin
public class ComboController {
	
	@Autowired
	private ComboService comboService;
	
	@PostMapping(value="pos/createCb")
	public BasicRes createCombo(CreateCbReq req) {
		return comboService.createCombo(req);
	}
	
	@PostMapping(value="pos/updateCb")
	public BasicRes updateCombo(UpdateCbReq req) {
		return comboService.updateCombo(req);
	}
	
	@PostMapping(value="pos/deleteCb")
	public BasicRes deleteCombo(DeleteCbReq req) {
		return comboService.deleteCombo(req);
	}
	@PostMapping(value="pos/searchCb")
	public BasicRes searchCombo(SearchCbReq req) {
		return comboService.searchCombo(req);
	}
	
}
