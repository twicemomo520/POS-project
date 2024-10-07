package com.example.pos10.service.ifs;

import com.example.pos10.vo.AnalysisReq;
import com.example.pos10.vo.AnalysisRes;
import com.example.pos10.vo.BasicRes;

public interface AnalysisService {
	
	public AnalysisRes analysis(AnalysisReq req);
}
