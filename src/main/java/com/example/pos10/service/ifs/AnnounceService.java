package com.example.pos10.service.ifs;

import com.example.pos10.vo.AnnounceDeleteReq;
import com.example.pos10.vo.AnnounceReq;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.SearchAnnounceRes;

public interface AnnounceService {

	public BasicRes createAnnounce(AnnounceReq req);
	
	public BasicRes updateAnnounce(AnnounceReq req);
	
	public BasicRes deleteAnnounce(AnnounceDeleteReq req);
	
	public SearchAnnounceRes searchAnnounce(AnnounceDeleteReq req);
}
