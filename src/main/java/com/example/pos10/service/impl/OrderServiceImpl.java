package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Categories;
import com.example.pos10.entity.ComboItems;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;
import com.example.pos10.entity.Orders;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.CategoriesDao;
import com.example.pos10.repository.ComboDao;
import com.example.pos10.repository.MenuItemsDao;
import com.example.pos10.repository.OptionsDao;
import com.example.pos10.repository.OrderDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.OrderService;
import com.example.pos10.vo.AddOrderReq;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ComboDetailVo;
import com.example.pos10.vo.ComboVo;
import com.example.pos10.vo.DishVo;
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

	@Autowired
	private CategoriesDao categoriesDao;

	@Autowired
	private MenuItemsDao menuItemsDao;

	@Autowired
	private OptionsDao optionsDao;

	@Autowired
	private TableManagementDao tableManagementDao;

	@Autowired
	private ComboDao comboDao;

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

		LocalDateTime startDate = LocalDateTime.parse(startDateInput + "T00:00:00",
				DateTimeFormatter.ISO_LOCAL_DATE_TIME);

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
			if (options != null && !options.isEmpty()) {
			    optionsList = Arrays.asList(options.split(","));
			} else {
			    // 处理 options 为 null 的情况
			    System.out.println("Options is null or empty");
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
	@Override
	public OrderMenuRes getOrderMenu() {

		List<Categories> categoriesList = categoriesDao.selectAll();

		// 放一個菜單裡面有價格跟工作台的map
		Map<Integer, Integer> cgIdAndwkId = new HashMap<>();

		// cgID對應的工作檯ID
		for (Categories data : categoriesList) {
//			cgIdAndwkId.put(data.getCategoryId(), data.getWorkstationId());

			Integer workstationId = data.getWorkstationId();
			if (workstationId != null) {
				cgIdAndwkId.put(data.getCategoryId(), workstationId);
			}

		}

		List<MenuItems> menuItemList = menuItemsDao.selectAll();

		// 菜單名稱對應的價格
		Map<String, Integer> mealNameAndPrice = new HashMap<>();

		for (MenuItems data : menuItemList) {
//			mealNameAndPrice.put(data.getMealName(), data.getPrice());

			String mealName = data.getMealName();
			Integer price = data.getPrice();
			if (mealName != null) {
				mealNameAndPrice.put(mealName, price);
			}

		}

		List<Options> optionsData = optionsDao.selectAll();

		// 使用 Map 來儲存每個 optionTitle 和 categoryId 的選項
		Map<String, OptionVo> organizedOptions = new HashMap<>();

		for (Options option : optionsData) {
			String key = option.getCategoryId() + "-" + option.getOptionTitle();

			// 檢查是否已存在該 optionTitle 和 categoryId 的key
			if (!organizedOptions.containsKey(key)) {
				List<OptionItemVo> optionItems = new ArrayList<>();

				OptionVo optionVo = new OptionVo();
				optionVo.setCategoryId(option.getCategoryId());
				optionVo.setOptionTitle(option.getOptionTitle());
				optionVo.setOptionType(option.getOptionType());
				optionVo.setOptionItems(optionItems);

				organizedOptions.put(key, optionVo);
			}

			// 新增每個選項的內容
			OptionItemVo item = new OptionItemVo();
			item.setOptionContent(option.getOptionContent());
			item.setExtraPrice(option.getExtraPrice());

			organizedOptions.get(key).getOptionItems().add(item);
		}

		// 將 Map 的值轉換為 List
		List<OptionVo> optionList = new ArrayList<>(organizedOptions.values());

		List<ComboVo> comboList = new ArrayList<>();

		List<ComboItems> comboItems = comboDao.selectAll();

		for (ComboItems data : comboItems) {

			ComboVo comboVo = new ComboVo();

			comboVo.setCategoryId(data.getCategoryId());
			comboVo.setComboName(data.getComboName());
			comboVo.setDiscountAmount(data.getDiscountAmount());

//        	cgIdAndwkId 對應工作臺ID
//        	mealNameAndPrice 菜單名稱對應價格

			JSONArray categoriesArray = new JSONArray(data.getComboDetail());

			List<ComboDetailVo> comboDetailVoList = new ArrayList<>();

			for (int i = 0; i < categoriesArray.length(); i++) {
				JSONObject categoryObj = categoriesArray.getJSONObject(i);
				int categoryId = categoryObj.getInt("categoryId");

				JSONArray dishesArray = categoryObj.getJSONArray("dishes");
				List<DishVo> dishVoList = new ArrayList<>();
				// 可以繼續處理 dishesArray 裡的每個 dish
				for (int j = 0; j < dishesArray.length(); j++) {
					// 注意：這裡使用 dishesArray.getString(j)，因為 dishesArray 裡是字串而不是 JSONObject
					String dishName = dishesArray.getString(j);

					DishVo dishVo = new DishVo();
					dishVo.setDishName(dishName);

					//dishVo.setPrice(mealNameAndPrice.get(dishName));
					Integer price = mealNameAndPrice.get(dishName);
					if (price != null) {
					    dishVo.setPrice(price);
					} else {
					    // 設置默認價格或記錄錯誤
					    dishVo.setPrice(0); // 或者其他預設值
					    System.out.println("No price found for dish: " + dishName);
					}

					dishVoList.add(dishVo);

				}

				ComboDetailVo comboDetailVo = new ComboDetailVo();

				comboDetailVo.setCategoryId(categoryId);
				comboDetailVo.setDishesList(dishVoList);

				comboDetailVo.setWorkstationId(cgIdAndwkId.get(categoryId));

				comboDetailVoList.add(comboDetailVo);
			}

			comboVo.setComboDetail(comboDetailVoList);

			comboList.add(comboVo);

		}

		List<String> tableNumberList = new ArrayList<>();
		List<TableManagement> tableData = tableManagementDao.selectAll();

		for (TableManagement data : tableData) {
			tableNumberList.add(data.getTableNumber());

		}

		return new OrderMenuRes(ResMessage.SUCCESS.getCode(), "成功回傳菜單資料", categoriesList, menuItemList, optionList,
				comboList, tableNumberList);
	}

	@Override
	public BasicRes addOrder(AddOrderReq req) {
		 // 從 AddOrderReq 中提取訂單列表
        List<Orders> ordersList = req.getOrdersList();
        // 驗證資料
        for (Orders order : ordersList) {
            if (!"準備中".equals(order.getMealStatus())) {
                return new BasicRes(400, "餐點狀態必須為 '準備中'");
            }
            if (order.getCheckout() != Boolean.FALSE) {
                return new BasicRes(400, "Checkout 必須為 false");
            }
        }

        // 批次儲存所有訂單
        orderDao.saveAll(ordersList);
        return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}


}
