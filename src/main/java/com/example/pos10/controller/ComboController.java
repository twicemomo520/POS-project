package com.example.pos10.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.ComboService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.DeleteCbReq;
import com.example.pos10.vo.SearchCbReq;
import com.example.pos10.vo.SearchCbRes;
import com.example.pos10.vo.UpdateCbReq;


@RestController
@CrossOrigin
public class ComboController {
	
	@Autowired
	private ComboService comboService;
	
	@PostMapping(value="pos/createCombo")
	public BasicRes createCombo(@Valid @RequestBody CreateCbReq req) {
		return comboService.createCombo(req);
	}
	
	@PostMapping(value="pos/updateCombo")
	public BasicRes updateCombo(@Valid @RequestBody UpdateCbReq req) {
		return comboService.updateCombo(req);
	}
	
	@PostMapping(value="pos/deleteCombo")
	public BasicRes deleteCombo(@Valid @RequestBody DeleteCbReq req) {
		return comboService.deleteCombo(req);
	}
	@PostMapping(value="pos/searchCombo")
	public SearchCbRes searchCombo(@Valid @RequestBody SearchCbReq req) {
		return comboService.searchCombo(req);
	}
	
	@GetMapping(value = "pos/countComboData")
	public int countData() {
		return comboService.countData();
	}
	
}
