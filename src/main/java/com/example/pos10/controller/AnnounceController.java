package com.example.pos10.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.AnnounceService;
import com.example.pos10.vo.AnnounceDeleteReq;
import com.example.pos10.vo.AnnounceReq;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.SearchAnnounceRes;

@RestController
@CrossOrigin
public class AnnounceController {

	@Autowired
	private AnnounceService announceService;

	@PostMapping(value = "announce/create")
	public BasicRes create(@Valid @RequestBody AnnounceReq req) {
		return announceService.createAnnounce(req);
	}

	@PostMapping(value = "announce/update")
	public BasicRes update(@Valid @RequestBody AnnounceReq req) {
		return announceService.updateAnnounce(req);
	}

	@PostMapping(value = "announce/delete")
	public BasicRes delete(@Valid @RequestBody AnnounceDeleteReq req) {
		return announceService.deleteAnnounce(req);
	}

	@PostMapping(value = "announce/searchAnnounce")
	public SearchAnnounceRes searchAnnounce(@Valid @RequestBody AnnounceDeleteReq req) {
		return announceService.searchAnnounce(req);
	}

}
