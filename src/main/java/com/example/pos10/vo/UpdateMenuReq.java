package com.example.pos10.vo;

import com.example.pos10.entity.MenuItems;

public class UpdateMenuReq extends MenuItems {

	public UpdateMenuReq() {
		super();
	}

	public UpdateMenuReq(String mealName, int categoryId, int workstationId, int price,
			boolean available, String pictureName) {
		super(mealName, categoryId, workstationId, price, available, pictureName);
	}
	
}
