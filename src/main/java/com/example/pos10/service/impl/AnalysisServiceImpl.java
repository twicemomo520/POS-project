package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.ComboItems;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.repository.OrdersHistoryDao;
import com.example.pos10.service.ifs.AnalysisService;
import com.example.pos10.vo.AnalysisPopularDishes;
import com.example.pos10.vo.AnalysisReq;
import com.example.pos10.vo.AnalysisRes;
import com.example.pos10.vo.AnalysisRevenueGrowth;
import com.example.pos10.vo.AnalysisMealVo;
import com.example.pos10.vo.AnalysisVo;
import com.example.pos10.vo.JoinOrderHistoryVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnalysisServiceImpl implements AnalysisService{
	
	@Autowired
	private OrdersHistoryDao ordersHistoryDao;
	
	
    public String checkMealType(String mealName) {
        // 查詢套餐表
		ComboItems comboItems = ordersHistoryDao.findComboItems(mealName);
        if (comboItems != null) {
            return "套餐";
        }
        // 查詢單點表
        MenuItems menuItems = ordersHistoryDao.findMenuItems(mealName);
        if (menuItems != null) {
            return "單點";
        }
        // 如果兩個表都沒有找到該餐點
        return "未找到此餐點";
    }
	
	@Override
	public AnalysisRes analysis(AnalysisReq req) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		LocalDate startDateInput = req.getStartDate();
		LocalDate endDateInput = req.getEndDate();
		String mealNameInput = req.getMealName();
		System.out.println("mealNameInput" + mealNameInput);
		
		if (startDateInput == null) {
			startDateInput = LocalDate.of(1900, 1, 1);
		}

		if (endDateInput == null) {
			endDateInput = LocalDate.of(2900, 1, 1);
		}

		LocalDateTime startDate = LocalDateTime.parse(startDateInput + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime endDate = LocalDateTime.parse(endDateInput + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		List<JoinOrderHistoryVo>orderDetailHistoryList = ordersHistoryDao.searchOrderDetailHistory(startDate, endDate);
		
		System.out.println(orderDetailHistoryList);
		
		String mealType = checkMealType(mealNameInput);
		System.out.println("mealType" + mealType);
		
		
		
		List<AnalysisPopularDishes>popularDishesList = new ArrayList<>();
		List<AnalysisRevenueGrowth>revenueGrowthList = new ArrayList<>();
		AnalysisMealVo analysisMealVo = new AnalysisMealVo();
		
		List<String> orderMealIdList = new ArrayList<>();		
		List<String>temporaryList = new ArrayList<>();		
		long totalRevenue=0;
		long totalOrders=0;
		
		//假如有輸入商品的話
		String mealName = mealNameInput;
		analysisMealVo.setMealName(mealName);
		
		
		for(JoinOrderHistoryVo orderHistoryItem: orderDetailHistoryList) {
				
				if(mealType.equals("套餐")) {
					String comboName = orderHistoryItem.getComboName();
					String mealDetail = orderHistoryItem.getMealName();
					if(Objects.equals(comboName, mealNameInput) &&  mealDetail == null) {
						
						analysisMealVo.setMealTotalOrders(analysisMealVo.getMealTotalOrders()+1);
					}
					if(Objects.equals(comboName, mealNameInput)) {
						
						int price = orderHistoryItem.getPrice();
						analysisMealVo.setMealTotalRevenue(analysisMealVo.getMealTotalRevenue() +price);
					}
				}
				
				if(mealType.equals("單點")) {
					String comboName = orderHistoryItem.getComboName();
					String mealDetail = orderHistoryItem.getMealName();
//					List<String> mealDetailOptionsList = new ArrayList<>();
//					try {
//						Map<String, List<String>> mealDetailMap = objectMapper.readValue(mealDetail, Map.class);
//			        	 mealDetailName = mealDetailMap.keySet().iterator().next();
//			        	 mealDetailOptionsList = mealDetailMap.get(mealDetailName);
//					}catch (JsonProcessingException e) {
//		                 System.out.println("JSON 解析錯誤: " + e.getMessage());
//		                 e.printStackTrace();
//		             }
					
					if((comboName == null) && mealDetail.equals(mealNameInput)) {
						
						analysisMealVo.setMealTotalOrders(analysisMealVo.getMealTotalOrders()+1);
						
						int price = orderHistoryItem.getPrice();
						analysisMealVo.setMealTotalRevenue(analysisMealVo.getMealTotalRevenue() +price);
					}
				}

			//處理totalRevenue
			totalRevenue+=orderHistoryItem.getPrice();
			
			//處理totalOrders
			String orderMealId = orderHistoryItem.getOrderMealId(); 
			if (!temporaryList.contains(orderMealId)) {
				temporaryList.add(orderMealId);
				totalOrders+=1;
			}
			
			
			//第一步 算出每日營業額
			LocalDateTime checkoutTime = orderHistoryItem.getCheckoutTime();
			LocalDate checkoutDate = checkoutTime.toLocalDate();
			int price = orderHistoryItem.getPrice();
			
			boolean found = false; // 用於標記是否找到匹配的日期
			
			for (AnalysisRevenueGrowth revenueGrowth: revenueGrowthList) {
				 if (checkoutDate.equals(revenueGrowth.getDay())) {
					 revenueGrowth.setRevenue(revenueGrowth.getRevenue() + orderHistoryItem.getPrice());
					 found = true;
					 break;
				 }
			}
			if(!found) {
				revenueGrowthList.add(new AnalysisRevenueGrowth(checkoutDate, orderHistoryItem.getPrice()));
			}
			
			
			
			//第二步 算出每個餐點銷售數量
			 String comboName = orderHistoryItem.getComboName(); // 假設套餐名作為菜品名
			 String singleName =  orderHistoryItem.getMealName();
			 String mealDetailName = comboName; //儲存菜品名的變數，先賦值為 comboName
			 List<String> mealDetailOptionsList;
			 
			 if (comboName == null) {
				 mealDetailName = singleName;
			        	// 檢查 popularDishes 中是否已經有該菜品
			        	boolean dishExists = false;
		                for (AnalysisPopularDishes popularDishes : popularDishesList) {
		                    if (popularDishes.getName() != null && popularDishes.getName().equals(mealDetailName)) {
		                        // 如果找到相同的菜品，訂單數量加 1
		                    	popularDishes.setOrders(popularDishes.getOrders() + 1);
		                        dishExists = true;
		                        break;
		                    }
		                }
		                if(!dishExists) {
		                	AnalysisPopularDishes newDish = new AnalysisPopularDishes(mealDetailName, 1);
		                	popularDishesList.add(newDish);
		                }
				 }   
	            //是套餐時    
                else {
                	
                	//確認有無這道菜
            		AnalysisPopularDishes popularDishExist = popularDishesList.stream()
            			    .filter(popularDish -> popularDish.getName().equals(comboName))
            			    .findFirst()  // 查找第一个匹配的元素
            			    .orElse(null);  // 如果没有找到则返回 null

                		//如果沒有這道菜
                		if(popularDishExist==null) {
                			popularDishesList.add(new AnalysisPopularDishes(comboName, 1));
                			orderMealIdList.add(orderHistoryItem.getOrderMealId());
                		}
                		
                		
                		//如果有這道菜	    
    		        	//該套餐在orderMealIdList李還沒有出現過才可以數量+1
    	                for (AnalysisPopularDishes popularDishes : popularDishesList) {
    	                    if (popularDishes.getName() != null && popularDishes.getName().equals(comboName) && !orderMealIdList.contains(orderHistoryItem.getOrderMealId())) {
    	                        // 如果找到相同的菜品，訂單數量加 1
    	                    	popularDishes.setOrders(popularDishes.getOrders() + 1);
    	                    	orderMealIdList.add(orderHistoryItem.getOrderMealId());
    	                        break;
    	                    }
    	                }                		
                	}

		}
	
		return new AnalysisRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),//
				new AnalysisVo(totalRevenue, totalOrders, popularDishesList, revenueGrowthList, analysisMealVo ));
	};

}
