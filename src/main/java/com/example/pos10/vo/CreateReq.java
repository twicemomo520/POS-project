package com.example.pos10.vo;

import java.util.List;

import javax.validation.Valid;

import com.example.pos10.entity.Categories;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;

public class CreateReq extends MenuItems {
	
	@Valid
	private List<Categories> cateList;
	
	@Valid
	private List<Options> optionsList;
	
	public CreateReq() {
		super();
	}

	public CreateReq(String mealName, String mealDescription, int categoryId, int workstationId, int price,
			boolean available, boolean featured, String pictureName) {
		super(mealName, mealDescription, categoryId, workstationId, price, available, featured, pictureName);
	}

	public List<Categories> getCateList() {
		return cateList;
	}

	public void setCateList(List<Categories> cateList) {
		this.cateList = cateList;
	}

	public List<Options> getOptionsList() {
		return optionsList;
	}

	public void setOptionsList(List<Options> optionsList) {
		this.optionsList = optionsList;
	}


}
