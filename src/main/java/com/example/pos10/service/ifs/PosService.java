package com.example.pos10.service.ifs;

import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.CreateCgReq;
import com.example.pos10.vo.CreateReq;
import com.example.pos10.vo.PosStatisticsReq;
import com.example.pos10.vo.PosStatisticsRes;

public interface PosService {

	public PosStatisticsRes statistics(PosStatisticsReq req);
	
	public BasicRes create(CreateReq req); // �Ыص��(willy)
	
	public BasicRes createCategory(CreateCgReq req);
	
	public BasicRes createCombo(CreateCbReq req);
	
}
