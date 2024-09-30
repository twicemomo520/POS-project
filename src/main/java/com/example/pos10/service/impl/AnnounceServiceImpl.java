package com.example.pos10.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Announce;
import com.example.pos10.repository.AnnounceDao;
import com.example.pos10.service.ifs.AnnounceService;
import com.example.pos10.vo.AnnounceDeleteReq;
import com.example.pos10.vo.AnnounceReq;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.SearchAnnounceRes;

@Service
public class AnnounceServiceImpl implements AnnounceService {

	@Autowired
	private AnnounceDao announceDao;

	@Override
	public BasicRes createAnnounce(AnnounceReq req) {
		Announce announce = new Announce(req.getAnnounceTitle(), req.getAnnounceContent(),
				req.getAnnouncePictureName(), req.getAnnounceStartTime(), req.getAnnounceEndTime());
		announceDao.save(announce);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Transactional
	@Override
	public BasicRes updateAnnounce(AnnounceReq req) {
		announceDao.updateAnnounce(req.getAnnounceId(), req.getAnnounceTitle(), req.getAnnounceContent(),
				req.getAnnouncePictureName(), req.getAnnounceStartTime(), req.getAnnounceEndTime());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public BasicRes deleteAnnounce(AnnounceDeleteReq req) {
		 announceDao.deleteAnnounce(req.getAnnounceId());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public SearchAnnounceRes searchAnnounce(AnnounceDeleteReq req) {
		List<Announce> announces = announceDao.searchAnnounce(req.getAnnounceId());
		 if (announces.isEmpty()) {
			 return new SearchAnnounceRes(ResMessage.ANNOUNCE_NOT_FOUND.getCode(),ResMessage.ANNOUNCE_NOT_FOUND.getMessage());
		 }
		 return new SearchAnnounceRes(ResMessage.SUCCESS.getCode(),ResMessage.SUCCESS.getMessage(),announces);
	}
	
	
}
