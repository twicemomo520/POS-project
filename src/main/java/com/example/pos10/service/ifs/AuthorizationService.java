package com.example.pos10.service.ifs;

import java.util.List;

import com.example.pos10.entity.Authorization;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.InsertAuthorizationReq;
import com.example.pos10.vo.UpdateAuthorizationReq;

public interface AuthorizationService {

	public BasicRes insertAuthorization(InsertAuthorizationReq req);
	
	public List<Authorization> findAllAuthorizations();

    public BasicRes updateAuthorization(UpdateAuthorizationReq req);

    public BasicRes deleteAuthorization(int id);
}
