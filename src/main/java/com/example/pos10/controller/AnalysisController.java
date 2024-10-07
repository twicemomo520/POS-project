package com.example.pos10.controller;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.AnalysisService;
import com.example.pos10.vo.AnalysisReq;
import com.example.pos10.vo.AnalysisRes;


@RestController
@CrossOrigin
public class AnalysisController {
	
	@Autowired
	private AnalysisService analysisService;
	
	@PostMapping(value="pos/analysis")
	public AnalysisRes analysis(@Valid @RequestBody AnalysisReq req) {
		return analysisService.analysis(req);
	}

}
