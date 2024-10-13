package com.example.pos10.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.CheckoutListSchedulerService;
import com.example.pos10.service.ifs.CheckoutService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ConfirmPaymentReq;
import com.example.pos10.vo.SearchCheckoutListRes;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

	@Autowired
	private CheckoutService checkoutService;
	
	@Autowired
	private CheckoutListSchedulerService checkoutListSchedulerService;

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
	
	
	
	// 用日期查詢
    @GetMapping("/list/{date}")
    public SearchCheckoutListRes searchCheckoutList(@PathVariable String date) {
        // 將字符串日期轉換為 LocalDate
        LocalDate searchDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return checkoutService.searchCheckoutList(searchDate);
    }
    
    
	@GetMapping("/detailsOrderId/{orderId}")
	public BasicRes searchCheckoutDetailByOrderId(@PathVariable String orderId) {
		// 調用 service 層的方法來獲取結帳詳細資訊
		return checkoutService.searchCheckoutDetailByOrderId(orderId);
	}
	
	
	@GetMapping("/scheduled")
    public BasicRes manualUpdateHistory() {
        return checkoutListSchedulerService.manualUpdateHistory();
    }
	
}
