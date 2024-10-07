package com.example.pos10.vo;

import java.time.LocalTime;
import java.util.List;
import com.example.pos10.entity.BusinessHours;

public class BusinessHoursRes {

    private int code; 
    private String message; 
    private List <BusinessHours> businessHoursList;  // 營業時間列表
    private int id;  // 新增的 ID 欄位
    private String dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    
    public BusinessHoursRes () {
        super ();
    }

    public BusinessHoursRes (int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessHoursRes (int code, String message, List <BusinessHours> businessHoursList) {
        super ();
        this.code = code;
        this.message = message;
        this.businessHoursList = businessHoursList;
    }

    public int getCode () {
        return code;
    }

    public void setCode (int code) {
        this.code = code;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public List <BusinessHours> getBusinessHoursList() {
        return businessHoursList;
    }

    public void setBusinessHoursList (List <BusinessHours> businessHoursList) {
        this.businessHoursList = businessHoursList;
    }
    
	public void setId (int id) {
		this.id = id;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setOpeningTime(LocalTime openingTime) {
		this.openingTime = openingTime;
	}

	public void setClosingTime(LocalTime closingTime) {
		this.closingTime = closingTime;
	}

	public int getId() {
		return id;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public LocalTime getOpeningTime() {
		return openingTime;
	}

	public LocalTime getClosingTime() {
		return closingTime;
	}

	
    
}