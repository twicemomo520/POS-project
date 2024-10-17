package com.example.pos10.service.ifs;

import java.util.List;

import com.example.pos10.vo.SearchOrderStatusRes;
import com.example.pos10.vo.SelectInPreparationRes;
import com.example.pos10.vo.UpdateOrderReq;
import com.example.pos10.vo.AddOrderReq;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.OrderMenuRes;
import com.example.pos10.vo.SearchOrderReq;

public interface OrderService {
	
	public SearchOrderStatusRes searchOrderStatus(SearchOrderReq req);
	
	public BasicRes updateOrderStatus(UpdateOrderReq req);
	
	public SelectInPreparationRes selectInPreparation();
	
	public BasicRes updateInPreparation(UpdateOrderReq req);
	
	public OrderMenuRes getOrderMenu();
	
	public BasicRes addOrder(AddOrderReq req);
	
}
