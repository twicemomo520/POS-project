package com.example.pos10.service.ifs;

import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.OperatingHours;
import com.example.pos10.vo.OperatingHoursReq;
import com.example.pos10.vo.OperatingHoursRes;

public interface OperatingHoursService {

    // 1. 新增或更新營業時間（單筆或批量）
    public List <OperatingHoursRes> saveOperatingHours (List <OperatingHoursReq> operatingHoursReqList);
    
    // 2. 計算可用的預約時間段
    public List <LocalTime> calculateAvailableTimeSlots(LocalTime openingTime, LocalTime closingTime, int diningDuration);

    // 3. 查詢店鋪的營業時間，dayOfWeek 可選
    public List <OperatingHours> getOperatingHours (String dayOfWeek);

    // 4. 刪除指定的營業時間
    public OperatingHoursRes deleteOperatingHours (List <Integer> ids);
}