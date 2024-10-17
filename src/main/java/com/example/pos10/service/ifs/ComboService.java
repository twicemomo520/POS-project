package com.example.pos10.service.ifs;

import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.DeleteCbReq;
import com.example.pos10.vo.SearchCbReq;
import com.example.pos10.vo.SearchCbRes;
import com.example.pos10.vo.UpdateCbReq;

public interface ComboService {
	
	public BasicRes createCombo(CreateCbReq req);
	
	public BasicRes updateCombo(UpdateCbReq req);
	
	public BasicRes deleteCombo(DeleteCbReq req);
	
	public SearchCbRes searchCombo(SearchCbReq req);
	
	public int countData();

}
