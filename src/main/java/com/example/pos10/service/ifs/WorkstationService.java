package com.example.pos10.service.ifs;

import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.SearchWorkRes;
import com.example.pos10.vo.WorkStationDeleteReq;
import com.example.pos10.vo.WorkStationReq;

public interface WorkstationService {
	
	public BasicRes workCreate(WorkStationReq req);
	
	public BasicRes workUpdate(WorkStationReq req);

	public BasicRes workDelete(WorkStationDeleteReq req);
	
	public SearchWorkRes workSearch(WorkStationDeleteReq req);
}
