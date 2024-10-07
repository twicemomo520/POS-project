package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.ComboItems;

public class SearchCbRes extends BasicRes{
	
	List<ComboItems> comboItemsList;

	public SearchCbRes() {
		super();
	}

	public SearchCbRes(int code, String message, List<ComboItems> comboItemsList) {
		super(code, message);
		
		this.comboItemsList = comboItemsList;
	}

	public List<ComboItems> getComboItemsList() {
		return comboItemsList;
	}

	public void setComboItemsList(List<ComboItems> comboItemsList) {
		this.comboItemsList = comboItemsList;
	}
	
	
	

}
