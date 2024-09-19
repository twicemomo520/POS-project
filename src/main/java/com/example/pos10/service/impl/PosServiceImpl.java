package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.repository.OrderDetailHistoryDao;
import com.example.pos10.repository.OrderHistoryDao;
import com.example.pos10.service.ifs.PosService;
import com.example.pos10.vo.JoinOrderVo;
import com.example.pos10.vo.PosStatisticsReq;
import com.example.pos10.vo.PosStatisticsRes;

@Service
public class PosServiceImpl implements PosService{
	
	@Autowired
	private OrderDetailHistoryDao orderDetailHistoryDao;
//	@Autowired
//	private OrderHistoryDao orderHistoryDao;
	
	@Override
	public PosStatisticsRes statistics(PosStatisticsReq req) {
		
		
		LocalDate startDateInput = req.getStartDate();
		LocalDate endDateInput = req.getEndDate();
		

		if (startDateInput == null) {
			startDateInput = LocalDate.of(1900, 1, 1);
		}

		if (endDateInput == null) {
			endDateInput = LocalDate.of(1900, 1, 1);
		}
		

		LocalDateTime startDate = LocalDateTime.parse(startDateInput + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime endDate = LocalDateTime.parse(endDateInput + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		
		System.out.println(startDate);
		System.out.println(endDate);
		
		List<JoinOrderVo>searchRes = orderDetailHistoryDao.selectDate(startDate, endDate);
		
		return new PosStatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), searchRes);
	}

}
