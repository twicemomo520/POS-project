package com.example.POS.project.service.ifs;

import com.example.POS.project.entity.BusinessHours;
import com.example.POS.project.vo.BusinessHoursReq;
import com.example.POS.project.vo.BusinessHoursRes;

import java.util.List;

public interface BusinessHoursService {

	// 1. 新增或更新營業時間
	public BusinessHoursRes addOrUpdateBusinessHours (BusinessHoursReq businessHoursReq);

    // 2. 查詢指定店鋪的營業時間，dayOfWeek 可選
	public List <BusinessHours> getBusinessHours (int storeId, String dayOfWeek);

    // 3. 刪除指定的營業時間
	public BusinessHoursRes deleteBusinessHours (int id);

	// 4. 查詢所有的營業時間
	public List <BusinessHours> getAllBusinessHours(); 
}