package com.example.pos10.constants;

public enum ResMessage {
	
	SUCCESS(200, "Success!!"),//
	PLEASE_LOGIN_FIRST(400, "Please login first"),//
	MEAL_NAME_EXISTS(400, "Meal name exists !"),//
	MEAL_NAME_NOT_FOUND(404, "Meal name not found !"),//
	CATEGORYID_NOT_FOUND(404, "Category id not FOUND !"),//
	CATEGORY_ALREADY_EXISTS(400, "Category already exists !"),//
	CATEGORY_NOT_FOUND(404, "Category not found !"),//
	PRICE_CANNOT_BE_NEGATIVE(400, "Price cannot be negative !"),//
	EXTRA_PRICE_CANNOT_BE_NEGATIVE(400, "Extra price cannot be negative !"),//
	WORKSTATION_ID_NOT_FOUND(404, "WorkStation id not found !"),//
	OPTION_ID_NOT_FOUND(404, "Option id not found !"),//
	OPTION_COMBINATION_ALREADY_EXISTS(400, "Option combination already exists !"),//
	OPTION_COMBINATION_NOT_FOUND(404, "Option combination not found !"),//
	OPTION_TITLE_NOT_FOUND(404, "Option title not found !"),//
	ANNOUNCE_NOT_FOUND(400,"Announce not found"),//
	WORKSTATION_NOT_FOUND(400,"Workstation not found"),//
	WORKSTATION_ALREADY_EXISTS(400,"Workstation already exists"),//
	COMBO_INPUT_CANNOT_BE_NULL_OR_EMPTY(400,"Update combo all input cannot be null or empty"),//
	COMBO_NAME_EXISTS(400,"Combo name exists"),//
	COMBO_NAME_NOT_FOUND(400,"Combo name cannot found");
	
	
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
