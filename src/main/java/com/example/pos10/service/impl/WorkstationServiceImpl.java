package com.example.pos10.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Workstation;
import com.example.pos10.repository.WorkstationDao;
import com.example.pos10.service.ifs.WorkstationService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.SearchWorkRes;
import com.example.pos10.vo.WorkStationDeleteReq;
import com.example.pos10.vo.WorkStationReq;

@Service
public class WorkstationServiceImpl implements WorkstationService {

	@Autowired
	private WorkstationDao workstationDao;

	@Override
	public BasicRes workCreate(WorkStationReq req) {
		int count = workstationDao.countByWorkstationName(req.getWorkstationName());
		if (count > 0) {
			return new BasicRes(ResMessage.WORKSTATION_ALREADY_EXISTS.getCode(),
					ResMessage.WORKSTATION_ALREADY_EXISTS.getMessage());
		}
		Workstation workstation = new Workstation(req.getWorkstationName());
		workstationDao.save(workstation);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public BasicRes workUpdate(WorkStationReq req) {
		int count = workstationDao.countByWorkstationName(req.getWorkstationName());
		if (count > 0) {
			return new BasicRes(ResMessage.WORKSTATION_ALREADY_EXISTS.getCode(),
					ResMessage.WORKSTATION_ALREADY_EXISTS.getMessage());
		}
		Workstation workstation = new Workstation(req.getWorkstationName());
		workstationDao.updateWork(req.getWorkstationName(), req.getWorkstationId());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public BasicRes workDelete(WorkStationDeleteReq req) {
		workstationDao.deleteWork(req.getWorkstationId());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public SearchWorkRes workSearch(WorkStationDeleteReq req) {
		List<Workstation> workstation = workstationDao.searchWorkstation(req.getWorkstationId());
		if (workstation.isEmpty()) {
			return new SearchWorkRes(ResMessage.WORKSTATION_NOT_FOUND.getCode(),
					ResMessage.WORKSTATION_NOT_FOUND.getMessage());
		}
		return new SearchWorkRes(ResMessage.SUCCESS.getCode(),ResMessage.SUCCESS.getMessage(),workstation);
	}

}
