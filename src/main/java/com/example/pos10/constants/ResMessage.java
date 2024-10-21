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
	ANNOUNCE_NOT_FOUND(404,"Announce not found"),//
	WORKSTATION_NOT_FOUND(404,"Workstation not found"),//
	WORKSTATION_ALREADY_EXISTS(400,"Workstation already exists"),//
	COMBO_INPUT_CANNOT_BE_NULL_OR_EMPTY(400,"Update combo all input cannot be null or empty"),//
	COMBO_NAME_EXISTS(400,"Combo name exists"),//
	NO_ORDERS_FOUND(404,"No orders found"), //
	COMBO_NAME_NOT_FOUND(404,"Combo name cannot found"), //
	UPLOAD_FAILED(400, "Upload failed"),//
	
		
	// ReservationManagement
	NO_BUSINESS_HOURS_FOUND_FOR_DAY (400, "No business hours found for the selected day !!!"), // 當天無營業時間
	OUTSIDE_BUSINESS_HOURS (400, "The selected time is outside of business hours !!!"), // 選擇的時間在營業時間之外
	NULL_OR_EMPTY_RESERVATION_DATE (400, "Reservation date cannot be null or empty !!!"), // 預約日期不得為 null 或空
	RESERVATION_DATE_CANNOT_BE_IN_PAST (400, "Reservation date cannot be in the past !!!"), // 預約日期不能是過去的時間
	NO_RESERVED_TIME_SLOTS (400, "No reserved time slots found !!!"), // 找不到可以預約的時間段
	NO_AVAILABLE_DINING_DURATION (400, "No available dining duration for the selected time range !!!"), // 查詢的時間範圍內已經沒有可用的用餐時段
	
	// Reservation
	INVALID_CUSTOMER_NAME (400, "Customer name cannot be null or empty !!!"), // 顧客姓名不能為 null 或空白
	INVALID_PHONE_NUMBER_FORMAT (400, "Invalid phone number format !!!"), // 電話號碼格式不正確
	INVALID_EMAIL_FORMAT (400, "Invalid email address."), // 電子郵件格式不正確
	INVALID_CUSTOMER_GENDER (400, "Customer gender cannot be null or empty !!!"), // 顧客性別不能為空
	INVALID_RESERVATION_PEOPLE (400, "Reservation people must be greater than 0 !!!"), // 訂位人數必須大於 0
	EXCEEDS_TABLE_CAPACITY (400, "The number of people exceeds the table's maximum capacity !!!"), // 表示訂位人數超過了桌位的最大容納量
	DUPLICATE_RESERVATION (400, "You already have a reservation at the same time !!!"), // 重複的訂位
	NOT_ENOUGH_TABLE_CAPACITY (400, "Not enough table capacity for the reservation !!!"), // 桌位的總容量無法滿足所需人數
	NO_RESERVATIONS_FOUND (404, "No reservations found for the provided phone number !!!"), // 查詢結果為空時，告知使用者系統中未找到符合條件的訂位
	INVALID_TABLE_STATUS_FOR_CANCELLATION (400, "The table is not reserved and cannot be canceled !!!"), // 當前桌位狀態不為「預定中」無法進行取消操作
	TABLE_NOT_RESERVED (400, "The table is not in a reserved state !!!"), // 桌位當前不處於「預定中」狀態
	FAILED_TO_UPDATE_TABLE_STATUS (400, "Failed to update the table status to RESERVED or ACTIVE !!!"), // 更新桌位狀態失敗
	
	// Waitlist
	INVALID_WAITLIST_PEOPLE (400, "Waitlist people must be greater than 0 !!!"), // 候位人數必須大於 0
	INVALID_WAIT_TIME (400, "Wait time cannot be null !!!"), // 候位時間不得為 null
	DUPLICATE_WAITLIST (400, "You are already on the waitlist !!!"), // 顧客已經在候位列表中
	NO_WAITLIST_FOUND (404, "No waitlist found for the provided phone number !!!"), // 查詢結果為空時，告知使用者系統中未找到符合條件的候位
	WAITLIST_ALREADY_CANCELLED (400, "The waitlist entry has already been cancelled !!!"), // 該候位記錄已經取消
	WAITLIST_ALREADY_CHECKED_IN (400, "The waitlist entry has already been checked in !!!"), // 該候位已經報到	
	;
	
	
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
