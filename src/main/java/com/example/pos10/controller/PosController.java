package com.example.pos10.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.PosService;
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
	
}
