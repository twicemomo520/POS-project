package com.example.pos10.vo;

public class UpdateWorkstationReq {
	
	private int categoryId;
	
	private int workstationId;

	public UpdateWorkstationReq() {
		super();
	}

	public UpdateWorkstationReq(int categoryId, int workstationId) {
		super();
		this.categoryId = categoryId;
		this.workstationId = workstationId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public int getWorkstationId() {
		return workstationId;
	}

	
}
