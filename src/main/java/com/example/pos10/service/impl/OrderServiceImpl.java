package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Categories;
import com.example.pos10.entity.ComboItems;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;
import com.example.pos10.entity.Orders;
import com.example.pos10.repository.OrderDao;
import com.example.pos10.service.ifs.OrderService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ComboDetailVo;
import com.example.pos10.vo.ComboVo;
import com.example.pos10.vo.MealDetailVo;
import com.example.pos10.vo.MealVo;
import com.example.pos10.vo.OptionItemVo;
import com.example.pos10.vo.OptionVo;
import com.example.pos10.vo.OrderMenuRes;
import com.example.pos10.vo.SearchOrderReq;
import com.example.pos10.vo.SearchOrderStatusRes;
import com.example.pos10.vo.SearchOrderStatusVo;
import com.example.pos10.vo.SelectInPreparationRes;
import com.example.pos10.vo.UpdateOrderReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;

	@Override
	public SearchOrderStatusRes searchOrderStatus(SearchOrderReq req) {

		LocalDate startDateInput = req.getStartDate();
		LocalDate endDateInput = req.getEndDate();
		
		if (startDateInput == null) {
			startDateInput = LocalDate.of(1900, 1, 1);
		}

		if (endDateInput == null) {
			endDateInput = LocalDate.of(2900, 1, 1);
		}
		
		LocalDateTime startDate = LocalDateTime.parse(startDateInput + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		LocalDateTime endDate = LocalDateTime.parse(endDateInput + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

		System.out.println(startDate);
		System.out.println(endDate);

		// 創建三個不同狀態的列表
		ObjectMapper objectMapper = new ObjectMapper();
		List<SearchOrderStatusVo> preparingOrders = new ArrayList<>();
		List<SearchOrderStatusVo> undeliveredOrders = new ArrayList<>();
		List<SearchOrderStatusVo> deliveredOrders = new ArrayList<>();

		List<Orders> searchOrderList = orderDao.selectOrderDate(startDate, endDate);
		System.out.println(searchOrderList);
//		searchOrderList.forEach(searchOrder->{
		for (Orders searchOrder : searchOrderList) {

			System.out.println(searchOrder);

			if (searchOrder == null) {
				continue;
			}


			int id = searchOrder.getId();
			String tableNumber = searchOrder.getTableNumber();
			String orderId = searchOrder.getOrderId();
			String orderMealId = searchOrder.getOrderMealId();
			String comboName = searchOrder.getComboName();
			String mealName = searchOrder.getMealName();
			String options = searchOrder.getOptions();
			String mealStatus = searchOrder.getMealStatus();
			LocalDateTime orderTime = searchOrder.getOrderTime();

			// 如果comboName有值但mealDetail沒值就跳過
			if ((mealName == null)) {
				continue;
			}
			// 將mealDetail由String轉成Map格式
			List<String> optionsList = new ArrayList<>();
			try {
				optionsList = objectMapper.readValue(options, new TypeReference<List<String>>() {
				});
				System.out.println(optionsList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			// 將mealDetail由Map格式轉成MealDetailVo格式
			// mealDetailVo是從資料庫撈回來的餐點
			MealDetailVo mealDetailVo = new MealDetailVo();
			mealDetailVo.setId(id);
			mealDetailVo.setMealName(mealName);
			mealDetailVo.setOptions(optionsList);

/////////////////////////////////////////////準備中/////////////////////////////////////////////////////////

			if (mealStatus.equals("準備中")) {

				// 檢測有無同一個tableNumber和orderId的資料
				Optional<SearchOrderStatusVo> existingOrderOpt = preparingOrders.stream()
						.filter(preparingOrder -> preparingOrder.getTableNumber().equals(tableNumber)
								&& preparingOrder.getOrderId().equals(orderId))
						.findFirst();

				// 如果有同一個tableNumber和orderId的資料，就取出那一桌資料
				if (existingOrderOpt.isPresent()) {
					SearchOrderStatusVo existingOrder = existingOrderOpt.get();

					List<MealVo> mealList = existingOrder.getMealList();
					// 檢測同一桌有無相同的orderMealId
					Optional<MealVo> existingOrderMealIdOpt = mealList.stream()
							.filter(meal -> meal.getOrderMealId().equals(orderMealId)).findFirst();

					// 如果有相同orderMealId就取出那個map的mealDetail然後把新的餐點MealDetailVo的東新放進去
					if (existingOrderMealIdOpt.isPresent()) {
						MealVo existingOrderMealId = existingOrderMealIdOpt.get();
						List<MealDetailVo> mealDetailList = existingOrderMealId.getMealDetail();
						mealDetailList.add(mealDetailVo);
					}
					// 如果沒有相同orderMealId就創一個MealVo
					// map的mealDetail然後把新的餐點MealDetailVo的東西放進去mealDetail裡
					else {
						MealVo mealVo = new MealVo();
						mealVo.setOrderMealId(orderMealId); // 設置orderMealId
						mealVo.setComboName(comboName); // 設置套餐名稱
						List<MealDetailVo> mealDetailList = new ArrayList<>(); // 因為是List<MealDetailVo>所以要先創一個list再把mealDetailVo放進去
						mealDetailList.add(mealDetailVo);
						mealVo.setMealDetail(mealDetailList); // 設置mealDetail
						mealVo.setOrderTime(orderTime); // 設置訂單時間
						mealList.add(mealVo);
					}
				}
				// 如果沒有同一個tableNumber和orderId的資料，就自己創建一個
				else {
					SearchOrderStatusVo searchOrderStatusVo = new SearchOrderStatusVo();
					searchOrderStatusVo.setTableNumber(tableNumber);
					searchOrderStatusVo.setOrderId(orderId);
					List<MealVo> mealList = new ArrayList<>();
					MealVo mealVo = new MealVo();
					mealVo.setOrderMealId(orderMealId);
					mealVo.setComboName(comboName);

					List<MealDetailVo> mealDetailList = new ArrayList<>();
					mealDetailList.add(mealDetailVo);
					mealVo.setMealDetail(mealDetailList);
					mealVo.setOrderTime(orderTime);
					mealList.add(mealVo);
					searchOrderStatusVo.setMealList(mealList);

					preparingOrders.add(searchOrderStatusVo);
				}
			}

/////////////////////////////////////////////待送餐點/////////////////////////////////////////////////////////

			if (mealStatus.equals("待送餐點")) {

				// 檢測有無同一個tableNumber和orderId的資料
				Optional<SearchOrderStatusVo> existingOrderOpt = undeliveredOrders.stream()
						.filter(undeliveredOrder -> undeliveredOrder.getTableNumber().equals(tableNumber)
								&& undeliveredOrder.getOrderId().equals(orderId))
						.findFirst();

				// 如果有同一個tableNumber和orderId的資料，就取出那一桌資料
				if (existingOrderOpt.isPresent()) {
					SearchOrderStatusVo existingOrder = existingOrderOpt.get();

					List<MealVo> mealList = existingOrder.getMealList();
					// 檢測同一桌有無相同的orderMealId
					Optional<MealVo> existingOrderMealIdOpt = mealList.stream()
							.filter(meal -> meal.getOrderMealId().equals(orderMealId)).findFirst();

					// 如果有相同orderMealId就取出那個map的mealDetail然後把新的餐點MealDetailVo的東新放進去
					if (existingOrderMealIdOpt.isPresent()) {
						MealVo existingOrderMealId = existingOrderMealIdOpt.get();
						List<MealDetailVo> mealDetailList = existingOrderMealId.getMealDetail();
						mealDetailList.add(mealDetailVo);
					}
					// 如果沒有相同orderMealId就創一個MealVo
					// map的mealDetail然後把新的餐點MealDetailVo的東西放進去mealDetail裡
					else {
						MealVo mealVo = new MealVo();
						mealVo.setOrderMealId(orderMealId); // 設置orderMealId
						mealVo.setComboName(comboName); // 設置套餐名稱
						List<MealDetailVo> mealDetailList = new ArrayList<>(); // 因為是List<MealDetailVo>所以要先創一個list再把mealDetailVo放進去
						mealDetailList.add(mealDetailVo);
						mealVo.setMealDetail(mealDetailList); // 設置mealDetail
						mealVo.setOrderTime(orderTime); // 設置訂單時間
						mealList.add(mealVo);
					}
				}
				// 如果沒有同一個tableNumber和orderId的資料，就自己創建一個
				else {
					SearchOrderStatusVo searchOrderStatusVo = new SearchOrderStatusVo();
					searchOrderStatusVo.setTableNumber(tableNumber);
					searchOrderStatusVo.setOrderId(orderId);
					List<MealVo> mealList = new ArrayList<>();
					MealVo mealVo = new MealVo();
					mealVo.setOrderMealId(orderMealId);
					mealVo.setComboName(comboName);

					List<MealDetailVo> mealDetailList = new ArrayList<>();
					mealDetailList.add(mealDetailVo);
					mealVo.setMealDetail(mealDetailList);
					mealVo.setOrderTime(orderTime);
					mealList.add(mealVo);
					searchOrderStatusVo.setMealList(mealList);

					undeliveredOrders.add(searchOrderStatusVo);
				}
			} // 未送達的範圍

/////////////////////////////////////////////已送達/////////////////////////////////////////////////////////

			if (mealStatus.equals("已送達")) {

				// 檢測有無同一個tableNumber和orderId的資料
				Optional<SearchOrderStatusVo> existingOrderOpt = deliveredOrders.stream()
						.filter(deliveredOrder -> deliveredOrder.getTableNumber().equals(tableNumber)
								&& deliveredOrder.getOrderId().equals(orderId))
						.findFirst();

				// 如果有同一個tableNumber和orderId的資料，就取出那一桌資料
				if (existingOrderOpt.isPresent()) {
					SearchOrderStatusVo existingOrder = existingOrderOpt.get();

					List<MealVo> mealList = existingOrder.getMealList();
					// 檢測同一桌有無相同的orderMealId
					Optional<MealVo> existingOrderMealIdOpt = mealList.stream()
							.filter(meal -> meal.getOrderMealId().equals(orderMealId)).findFirst();

					// 如果有相同orderMealId就取出那個map的mealDetail然後把新的餐點MealDetailVo的東新放進去
					if (existingOrderMealIdOpt.isPresent()) {
						MealVo existingOrderMealId = existingOrderMealIdOpt.get();
						List<MealDetailVo> mealDetailList = existingOrderMealId.getMealDetail();
						mealDetailList.add(mealDetailVo);
					}
					// 如果沒有相同orderMealId就創一個MealVo
					// map的mealDetail然後把新的餐點MealDetailVo的東西放進去mealDetail裡
					else {
						MealVo mealVo = new MealVo();
						mealVo.setOrderMealId(orderMealId); // 設置orderMealId
						mealVo.setComboName(comboName); // 設置套餐名稱
						List<MealDetailVo> mealDetailList = new ArrayList<>(); // 因為是List<MealDetailVo>所以要先創一個list再把mealDetailVo放進去
						mealDetailList.add(mealDetailVo);
						mealVo.setMealDetail(mealDetailList); // 設置mealDetail
						mealVo.setOrderTime(orderTime); // 設置訂單時間
						mealList.add(mealVo);
					}
				}
				// 如果沒有同一個tableNumber和orderId的資料，就自己創建一個
				else {
					SearchOrderStatusVo searchOrderStatusVo = new SearchOrderStatusVo();
					searchOrderStatusVo.setTableNumber(tableNumber);
					searchOrderStatusVo.setOrderId(orderId);
					List<MealVo> mealList = new ArrayList<>();
					MealVo mealVo = new MealVo();
					mealVo.setOrderMealId(orderMealId);
					mealVo.setComboName(comboName);

					List<MealDetailVo> mealDetailList = new ArrayList<>();
					mealDetailList.add(mealDetailVo);
					mealVo.setMealDetail(mealDetailList);
					mealVo.setOrderTime(orderTime);
					mealList.add(mealVo);
					searchOrderStatusVo.setMealList(mealList);

					deliveredOrders.add(searchOrderStatusVo);
				}
			}

		}
		;

		return new SearchOrderStatusRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), //
				preparingOrders, undeliveredOrders, deliveredOrders);

	}

	@Override
	public BasicRes updateOrderStatus(UpdateOrderReq req) {

		int id = req.getId();

		orderDao.updateOrderStatus(id);

		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public SelectInPreparationRes selectInPreparation() {
		List<Orders> preparingOrders = orderDao.selectInPreparation();
		if (preparingOrders.isEmpty()) {
			return new SelectInPreparationRes(ResMessage.NO_ORDERS_FOUND.getCode(),
					ResMessage.NO_ORDERS_FOUND.getMessage());
		}
		return new SelectInPreparationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),
				preparingOrders);
	}

	@Override
	public BasicRes updateInPreparation(UpdateOrderReq req) {

		int id = req.getId();

		orderDao.updateInPreparation(id);

		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	/////////// 撈菜單///////////////////////////

}
