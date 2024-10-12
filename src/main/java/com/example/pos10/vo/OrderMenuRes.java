package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.Categories;
import com.example.pos10.entity.MenuItems;

public class OrderMenuRes extends BasicRes {
	
	private List<Categories> categoriesList;
	
	private List<MenuItems> menuItemList;
	
	private List<OptionVo> optionList;
   
    private List<ComboVo> comboList;
    
    private List<String> tableNumberList;

	public OrderMenuRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderMenuRes(int code, String message, List<Categories> categoriesList, List<MenuItems> menuItemList, List<OptionVo> optionList,
			List<ComboVo> comboList, List<String> tableNumberList) {
		super(code, message);
		this.categoriesList = categoriesList;
		this.menuItemList = menuItemList;
		this.optionList = optionList;
		this.comboList = comboList;
		this.tableNumberList = tableNumberList;
	}

	public List<Categories> getCategoriesList() {
		return categoriesList;
	}

	public void setCategoriesList(List<Categories> categoriesList) {
		this.categoriesList = categoriesList;
	}

	public List<MenuItems> getMenuItemList() {
		return menuItemList;
	}

	public void setMenuItemList(List<MenuItems> menuItemList) {
		this.menuItemList = menuItemList;
	}

	public List<OptionVo> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<OptionVo> optionList) {
		this.optionList = optionList;
	}

	public List<ComboVo> getComboList() {
		return comboList;
	}

	public void setComboList(List<ComboVo> comboList) {
		this.comboList = comboList;
	}

	public List<String> getTableNumberList() {
		return tableNumberList;
	}

	public void setTableNumberList(List<String> tableNumberList) {
		this.tableNumberList = tableNumberList;
	}

}
