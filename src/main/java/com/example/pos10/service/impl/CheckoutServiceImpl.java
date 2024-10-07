package com.example.pos10.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.repository.CheckoutDao;
import com.example.pos10.service.ifs.CheckoutService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CheckoutDetailRes;

@Service
public class CheckoutServiceImpl implements CheckoutService {
	
	@Autowired CheckoutDao checkoutDao;

	@Override
	public BasicRes searchCheckoutDetail(String tableNumber) {
		
		
		
		List<CheckoutDetailRes> checkoutDetailRes = checkoutDao.findUnpaidOrdersByTableNumber(tableNumber);
		
		for (CheckoutDetailRes detail : checkoutDetailRes) {
		    System.out.println("Order ID: " + detail.getOrderId());
		    System.out.println("Order Meal ID: " + detail.getOrderMealId());
		    System.out.println("Combo Name: " + detail.getComboName());
		    System.out.println("Meal Name: " + detail.getMealName());
		    System.out.println("Options: " + detail.getOptions());
		    System.out.println("Price: " + detail.getPrice());
		    System.out.println("Table Number: " + detail.getTableNumber());
		    System.out.println("Checkout: " + detail.getCheckout());
		    System.out.println("-------------------------");
		}
		
		return new BasicRes(200, "成功");
	}

}
