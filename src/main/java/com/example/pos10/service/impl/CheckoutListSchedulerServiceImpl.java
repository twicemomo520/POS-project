package com.example.pos10.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.pos10.entity.CheckoutList;
import com.example.pos10.entity.CheckoutListHistory;
import com.example.pos10.entity.Orders;
import com.example.pos10.entity.OrdersHistory;
import com.example.pos10.repository.CheckoutListDao;
import com.example.pos10.repository.CheckoutListHistoryDao;
import com.example.pos10.repository.OrderDao;
import com.example.pos10.repository.OrdersHistoryDao;
import com.example.pos10.service.ifs.CheckoutListSchedulerService;
import com.example.pos10.vo.BasicRes;

@Service
public class CheckoutListSchedulerServiceImpl implements CheckoutListSchedulerService {

	@Autowired
	private CheckoutListDao checkoutListDao;

	@Autowired
	private CheckoutListHistoryDao checkoutListHistoryDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrdersHistoryDao ordersHistoryDao;

	// 自動排程方法
	@Scheduled(cron = "0 0 12 * * ?")
	public BasicRes scheduledUpdateHistory() {
		return performUpdateHistory();
	}

	// 手動觸發方法
	@Override
	public BasicRes manualUpdateHistory() {
		return performUpdateHistory();
	}

	// 更新歷史記錄的核心方法
	@Transactional
	private BasicRes performUpdateHistory() {
		List<CheckoutList> checkoutLists = checkoutListDao.findAll();

		// 將每條記錄移動到 checkoutListHistory
		List<CheckoutListHistory> historyList = new ArrayList<>();

		for (CheckoutList checkout : checkoutLists) {
			CheckoutListHistory history = new CheckoutListHistory(checkout.getOrderId(), checkout.getTableNumber(),
					checkout.getTotalPrice(), checkout.getPayType(), checkout.getCheckout(),
					checkout.getCheckoutTime());

			historyList.add(history);
		}

		// 批量保存歷史記錄
		checkoutListHistoryDao.saveAll(historyList);

		// 刪除原有的數據
		checkoutListDao.deleteAll();

		List<Orders> ordersList = orderDao.findAll(); // 將每條記錄移動到 checkoutList
		List<OrdersHistory> ordersHistoryList = new ArrayList<>();

		for (Orders order : ordersList) {
			OrdersHistory ordersHistory = new OrdersHistory(order.getId(), order.getOrderId(), order.getOrderMealId(),
					order.getComboName(), order.getMealName(), order.getOptions(), order.getWorkstationId(),
					order.getPrice(), order.getMealStatus(), order.getTableNumber(), order.getOrderTime(),
					order.getCheckout());
			ordersHistoryList.add(ordersHistory);
		}

		// 批量保存歷史記錄
		ordersHistoryDao.saveAll(ordersHistoryList);

		// 刪除原有的數據
		orderDao.deleteAll();

		return new BasicRes(200, "排程執行完成");
	}
}
