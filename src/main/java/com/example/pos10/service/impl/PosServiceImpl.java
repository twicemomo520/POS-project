package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;
import com.example.pos10.repository.CategoriesDao;
import com.example.pos10.repository.MenuItemsDao;
import com.example.pos10.repository.OptionsDao;
import com.example.pos10.repository.OrderDetailHistoryDao;
import com.example.pos10.service.ifs.PosService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.CreateCgReq;
import com.example.pos10.vo.CreateReq;
import com.example.pos10.vo.JoinOrderVo;
import com.example.pos10.vo.PosStatisticsReq;
import com.example.pos10.vo.PosStatisticsRes;

@Service
public class PosServiceImpl implements PosService{
	
	@Autowired
	private OrderDetailHistoryDao orderDetailHistoryDao;
//	@Autowired
//	private OrderHistoryDao orderHistoryDao;
	
	@Autowired
	private MenuItemsDao menuItemsDao;

	@Autowired
	private CategoriesDao categoriesDao;

	@Autowired
	private OptionsDao optionsDao;
	
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
	
	@Transactional
	@Override
	public BasicRes create(CreateReq req) {
		String mealName = req.getMealName();
		// 若資料庫找到一樣的mealName數量大於0，表示mealName存在於資料庫
		if (menuItemsDao.existsByMealName(mealName) > 0) {
			// 返回菜單已存在的錯誤訊息
			return new BasicRes(ResMessage.MEAL_NAME_EXISTS.getCode(), ResMessage.MEAL_NAME_EXISTS.getMessage());
		}
		int categoryId = req.getCategoryId();
		// 欲新增的categoryId必須已存在
		if(categoriesDao.optionExists(categoryId) == 0) {
			// 若不存在返回錯誤訊息
			return new BasicRes(ResMessage.CATEGORYID_NOT_FOUND.getCode(), ResMessage.CATEGORYID_NOT_FOUND.getMessage());
		}
		// 用Jpa的save存進資料庫，定義變數res
		MenuItems res = menuItemsDao.save(new MenuItems(mealName, req.getMealDescription(), categoryId, req.getWorkstationId(), req.getPrice()));
		List<Options> optionsList = req.getOptionsList();
		// 取出res的categoryId塞進optionsList的每一個cg_id
		optionsList.forEach(item ->{
			item.setCategoryId(res.getCategoryId());
		});
		// Jpa 的 saveAll(saveAll是陣列的存法)
		optionsDao.saveAll(optionsList);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public BasicRes createCategory(CreateCgReq req) {
		String categoryName = req.getCategory();
		// 新增餐點分類，新增的類別名稱不能已存在
		if(categoriesDao.cgNameExists(categoryName) > 0) {
			//回傳錯誤訊息
			return new BasicRes(ResMessage.CATEGORYID_ALREADY_EXISTS.getCode(), ResMessage.CATEGORYID_ALREADY_EXISTS.getMessage());
		}
		categoriesDao.insert(categoryName);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public BasicRes createCombo(CreateCbReq req) {
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
