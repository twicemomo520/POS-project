package com.example.pos10.service.ifs;

import java.util.Map;

import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ConfirmPaymentReq;

public interface CheckoutService {

	public BasicRes searchCheckoutDetail(String tableNumber);
	
	public BasicRes confirmPayment(ConfirmPaymentReq req);
	
}
