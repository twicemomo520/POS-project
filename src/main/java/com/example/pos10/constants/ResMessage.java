package com.example.pos10.constants;

public enum ResMessage {
	
	SUCCESS(200, "Success!!"),//
	PLEASE_LOGIN_FIRST(400, "Please login first"),//
	MEAL_NAME_EXISTS(400, "Meal name exists !"),//
	CATEGORYID_NOT_FOUND(404, "Category id not FOUND !"),//
	CATEGORYID_ALREADY_EXISTS(400, "Category already exists !"),
	ANNOUNCE_NOT_FOUND(400,"Announce not found"),
	WORKSTATION_ALREADY_EXISTS(400,"Workstation already exists");
	
	
	private int code;
	
	private String message;
	
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

	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

}
