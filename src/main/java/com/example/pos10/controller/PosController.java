package com.example.pos10.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.PosService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCgReq;
import com.example.pos10.vo.CreateReq;
import com.example.pos10.vo.PosStatisticsReq;
import com.example.pos10.vo.PosStatisticsRes;


@RestController
@CrossOrigin
public class PosController {
	
	@Autowired
	private PosService posService ;
	
	@PostMapping(value = "pos/statistics")
	public PosStatisticsRes statistics(@Valid @RequestBody  PosStatisticsReq req) {
		return posService.statistics(req);
	}
	
	@PostMapping(value = "menu/create")
	public BasicRes create(@Valid @RequestBody CreateReq req) {
		return posService.create(req);
	}

	@PostMapping(value = "menu/createCategory")
	public BasicRes createCategory(@Valid @RequestBody CreateCgReq req) {
		return posService.createCategory(req);
	}
	
}
