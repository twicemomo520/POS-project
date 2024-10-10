package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.CheckoutService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ConfirmPaymentReq;
import com.example.pos10.vo.RegisterStaffReq;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

	@Autowired
	private CheckoutService checkoutService;

	@GetMapping("/details/{tableNumber}")
	public BasicRes getCheckoutDetails(@PathVariable String tableNumber) {
		// 調用 service 層的方法來獲取結帳詳細資訊
		return checkoutService.searchCheckoutDetail(tableNumber);
	}

	// 結帳概要
	@PostMapping("/confirmPayment")
	public BasicRes confirmPayment(@RequestBody ConfirmPaymentReq req) {
		return checkoutService.confirmPayment(req);
	}

}
