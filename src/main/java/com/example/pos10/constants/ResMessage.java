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
	COMBO_NAME_NOT_FOUND(400,"Combo name cannot found"),//
	
	// TableManagement
	INVALID_TABLE_STATUS (400, "Invalid table status value !!!"), //
	NULL_OR_EMPTY_TABLE_STATUS (400, "Table status cannot be null or empty !!!"), //
	TABLE_NUMBER_ALREADY_EXIST (400, "Table number already exists !!!"), //
	INVALID_TABLE_CAPACITY (400, "Table capacity must be greater than 0 !!!"), //
	TABLE_NUMBER_NOT_FOUND (400, "Table number not found !!!"), //
	INVALID_TABLE_NUMBER_NAME (400, "Table number name is invalid !!!"), //
	TABLE_HAS_RESERVATION_ID (400, "Table has reservaion id !!!"), //
	TABLE_STATUS_IS_NOT_AVAILABLE (400, "Table status is not available !!!"), //
	INVALID_STATUS_TRANSITION (400, "Table status transition is invalid !!!"), //
	TABLE_RESERVATION_CONFLICT(400, "Table is already reserved by another reservation !!!"), //
	TABLE_STATUS_NOT_FOUND (400, "Table status not found !!!"), //
	TABLE_STATUS_NOT_ALLOWED_FOR_CAPACITY_CHANGE (400, "Table status not allowed for capacity change !!!"), //
	INVALID_CUSTOMER_COUNT (400, "Customer count is invalid !!!"), //
	NO_AVAILABLE_TABLES (400, "No available tables !!!"), // 沒有可使用中的位子
		
	// BusinessHour 營業時間
	INVALID_OPENING_AND_CLOSING_TIME (400, "Opening time must be earlier than closing time !!!"), // 開店時間必須早於關店時間
	CONFLICTING_BUSINESS_HOURS (400, "The specified time period conflicts with existing business hours !!!"), // 指定的時間段與現有的營業時間衝突
	BUSINESS_HOURS_TO_UPDATE_NOT_FOUND (400, "The business hours to update were not found !!!"), // 營業時間不存在無法進行更新操作
	BUSINESS_HOURS_NOT_FOUND_FOR_DELETION (400, "Cannot delete, the business hours ID does not exist."), // 無法刪除，不存在的營業時間 ID
		
	// DiningDuration 用餐時間
	NULL_OR_EMPTY_DINING_DURATION (400,"Dining duration cannot be null or empty !!!"), // 用餐時間不能為 null 或空白
	BUSINESS_HOURS_NOT_FOUND_FOR_DINING_DURATION (400, "The corresponding business hours for the dining duration do not exist !!!"), // 找不到對應營業時間
	DINING_DURATION_NOT_FOUND_FOR_UPDATE (400, "Dining duration does not exist, unable to update !!!"), // 用餐時間不存在無法更新
	DINING_DURATION_CONFLICT (400, "The dining duration overlaps with an existing record !!!"), // 用餐時間段與現有記錄重疊
	RESERVATION_EXISTS_IN_TIME_RANGE (400, "Reservations exist for the specified time range, unable to update !!!"), // 該時間段已有預訂無法更新用餐時間
	DINING_DURATION_EXCEEDS_BUSINESS_HOURS (400, "Dining duration cannot exceed business hours !!!"), // 用餐時間不能超過營業時間
	INVALID_STORE_ID (400, "Invalid store ID !!!"), // 無效的 storeID
	INVALID_DAY_OF_WEEK (400, "Invalid day of the week !!!"), // 無效的星期
	DINING_DURATION_NOT_FOUND (404, "Dining duration not found !!!"), // 找不到用餐時間
	INVALID_DINING_DURATION_ID (400, "Invalid dining duration ID !!!"), // 無效的用餐時間 ID
	DINING_DURATION_HAS_RESERVATIONS (400, "Cannot delete. There are reservations associated with this dining duration !!!"), // 用餐時間已經有人預定
	INVALID_TIME_FORMAT (400, "Invalid time format !!!"), // 時間格式無效
	INVALID_DINING_DURATION (400, "Start time must be earlier than end time !!!"), // 無效的用餐時間
		
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
	NO_RESERVATIONS_FOUND (404, "No reservations found for the provided phone number."), // 查詢結果為空時，告知使用者系統中未找到符合條件的訂位
	INVALID_TABLE_STATUS_FOR_CANCELLATION (400, "The table is not reserved and cannot be canceled."), // 當前桌位狀態不為「預定中」無法進行取消操作
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
