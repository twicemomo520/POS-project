package com.example.pos10.vo;

import java.util.List;

public class  OperatingHoursRes {

    private int code; 
    private String message;
    private List<Integer> deletedIds; // 儲存已刪除的 ID
    
	public OperatingHoursRes () {
		super ();
	}

	public OperatingHoursRes (int code, String message) {
		this.code = code;
		this.message = message;
	}

	public OperatingHoursRes (int code, String message, List< Integer> deletedIds) {
		this.code = code;
		this.message = message;
		this.deletedIds = deletedIds;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Integer> getDeletedIds() {
		return deletedIds;
	}

	public void setDeletedIds(List<Integer> deletedIds) {
		this.deletedIds = deletedIds;
	} 
}