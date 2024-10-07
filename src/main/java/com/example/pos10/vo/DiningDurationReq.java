package com.example.POS.project.vo;

import java.util.List;
import com.example.POS.project.entity.DiningDuration;

public class DiningDurationReq extends DiningDuration {

    private List <Integer> businessHoursIds; // 用來存儲多個營業時間的ID

    public DiningDurationReq () {
        super ();
    }
    
	public DiningDurationReq (int durationMinutes, List <Integer> businessHoursIds) {
		super ();
        this.businessHoursIds = businessHoursIds;
    }

    public List <Integer> getBusinessHoursIds () {
        return businessHoursIds;
    }

    public void setBusinessHoursIds (List<Integer> businessHoursIds) {
        this.businessHoursIds = businessHoursIds;
    }
}