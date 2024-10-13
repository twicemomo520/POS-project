package com.example.pos10.vo;

import java.util.List;

public class ComboDetailVo {
	
    private int categoryId;
    
    private int workstationId;
    
    private List<DishVo> dishesList;

	public ComboDetailVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ComboDetailVo(int categoryId, int workstationId, List<DishVo> dishesList) {
		super();
		this.categoryId = categoryId;
		this.workstationId = workstationId;
		this.dishesList = dishesList;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(int workstationId) {
		this.workstationId = workstationId;
	}

	public List<DishVo> getDishesList() {
		return dishesList;
	}

	public void setDishesList(List<DishVo> dishesList) {
		this.dishesList = dishesList;
	}
    
    

}
