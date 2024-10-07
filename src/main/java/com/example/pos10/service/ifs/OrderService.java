package com.example.pos10.service.ifs;

import java.util.List;

public interface OrderService {
	
	// 取得所有分類及其對應的餐點、客製化選項
	//  List<CategoryResponse> getAllCategoriesAndItems();
	
	public SearchOrderStatusRes searchOrderStatus(SerchORderReq req);

}
