package com.example.pos10.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.repository.CheckoutDao;
import com.example.pos10.service.ifs.CheckoutService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CheckoutDetailRes;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	CheckoutDao checkoutDao;

	class OrderMeal {
		private String comboName;
		private String mealName;
		private int price;

		public String getComboName() {
			return comboName;
		}

		public void setComboName(String comboName) {
			this.comboName = comboName;
		}

		public String getMealName() {
			return mealName;
		}

		public void setMealName(String mealName) {
			this.mealName = mealName;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public OrderMeal(String comboName, String mealName, int price) {
			this.comboName = comboName;
			this.mealName = mealName;
			this.price = price;
		}
	}

	class SingleItem {
		private String mealName;
		private int price;

		public String getMealName() {
			return mealName;
		}

		public void setMealName(String mealName) {
			this.mealName = mealName;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public SingleItem(String mealName, int price) {
			this.mealName = mealName;
			this.price = price;
		}
	}

	class Order {
		private String orderId;
		private List<OrderMeal> orderMealId;
		private List<SingleItem> single;
		private int totalPrice;

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public List<OrderMeal> getOrderMealId() {
			return orderMealId;
		}

		public void setOrderMealId(List<OrderMeal> orderMealId) {
			this.orderMealId = orderMealId;
		}

		public List<SingleItem> getSingle() {
			return single;
		}

		public void setSingle(List<SingleItem> single) {
			this.single = single;
		}

		public int getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}

		public Order(String orderId, List<OrderMeal> orderMealId, List<SingleItem> single, int totalPrice) {
			super();
			this.orderId = orderId;
			this.orderMealId = orderMealId;
			this.single = single;
			this.totalPrice = totalPrice;
		}

	}

	@Override
	public CheckoutDetailRes searchCheckoutDetail(String tableNumber) {
		
		List<Object[]> objectResult = checkoutDao.findUnpaidOrdersByTableNumber(tableNumber);

		if (objectResult.isEmpty()) {
			return new CheckoutDetailRes(400, "查無此桌號資料","");
		} else {
			// 用來存儲訂單ID
			String orderId = "";

			// 用來存儲餐點組合的映射
			Map<String, List<OrderMeal>> orderMealMap = new HashMap<>();

			// 用來存儲單點餐項
			List<SingleItem> singleItems = new ArrayList<>();

			for (Object[] row : objectResult) {
				// 獲取訂單ID
				if (orderId.isEmpty()) {
					orderId = (String) row[0]; // 只獲取第一個訂單的ID
				}

				String comboName = (String) row[2]; // 餐點組合名稱
				String mealName = (String) row[3]; // 餐點名稱
				String option = (String) row[4]; // 附加選項
				int price = (int) row[5]; // 價格

				// 構造餐點名稱 (加上附加選項)
				String mealWithOption = null;
				if (mealName != null) {
					mealWithOption = mealName;
					if (option != null) {
						mealWithOption += "(" + option + ")";
					}
				}

				// 如果 comboName 不為 null 且 mealName 不為 null，將其加入到 map 中
				if (comboName != null) {
					orderMealMap.putIfAbsent(comboName, new ArrayList<>()); // 若 map 中沒有此 comboName，則創建一個新的列表
					orderMealMap.get(comboName).add(new OrderMeal(comboName, mealWithOption, price));
				} else if (mealName != null) {
					// 這是單點餐點，且 mealName 不為 null
					singleItems.add(new SingleItem(mealWithOption, price));
				}
			}

			// 創建最終的 orderMealId 列表
			List<OrderMeal> orderMeals = new ArrayList<>();
			for (List<OrderMeal> meals : orderMealMap.values()) {
				// 將每一組的餐點合併成一個字符串
				StringBuilder combinedMealNames = new StringBuilder();
				int totalPrice = 0; // 計算總價格

				for (OrderMeal meal : meals) {
					if (meal.getMealName() != null) {
						combinedMealNames.append(meal.getMealName()).append(", "); // 合併餐點名稱
					}

					totalPrice += meal.getPrice(); // 累加價格
				}

				// 將最後一個逗號和空格移除
				if (combinedMealNames.length() > 0) {
					combinedMealNames.setLength(combinedMealNames.length() - 2); // 去除最後的 ", "
				}

				// 將合併後的餐點添加到 orderMeals 列表中
				orderMeals.add(new OrderMeal(meals.get(0).getComboName(), combinedMealNames.toString(), totalPrice));
			}

			int totalPrice = checkoutDao.findTotalUnpaidAmountByTableNumber(tableNumber);
			// 創建 order 對象
			Order order = new Order(orderId, orderMeals, singleItems, totalPrice);

			// 返回 CheckoutDetailRes，將訂單資料作為 data
			return new CheckoutDetailRes(200, "成功", order);
		}

	}

}

