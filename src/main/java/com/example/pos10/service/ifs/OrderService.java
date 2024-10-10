package com.example.pos10.service.ifs;

import java.util.List;

import com.example.pos10.vo.SearchOrderStatusRes;
import com.example.pos10.vo.SelectInPreparationRes;
import com.example.pos10.vo.UpdateOrderReq;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.SearchOrderReq;

public interface OrderService {
	
	// 取得所有分類及其對應的餐點、客製化選項
	//  List<CategoryResponse> getAllCategoriesAndItems();
	
	public SearchOrderStatusRes searchOrderStatus(SearchOrderReq req);
	
	public BasicRes updateOrderStatus(UpdateOrderReq req);
	
	public SelectInPreparationRes selectInPreparation();
	
	public BasicRes updateInPreparation(UpdateOrderReq req);

}
