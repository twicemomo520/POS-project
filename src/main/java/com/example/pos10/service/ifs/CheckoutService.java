package com.example.pos10.service.ifs;

import java.time.LocalDate;

import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ConfirmPaymentReq;
import com.example.pos10.vo.SearchCheckoutListRes;

public interface CheckoutService {

	public BasicRes searchCheckoutDetail(String tableNumber);
	
	public BasicRes confirmPayment(ConfirmPaymentReq req);
	
	public SearchCheckoutListRes searchCheckoutList(LocalDate searchDate);
	
	public BasicRes searchCheckoutDetailByOrderId(String orderId);
	
}
