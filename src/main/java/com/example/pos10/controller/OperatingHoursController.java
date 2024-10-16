package com.example.pos10.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.entity.OperatingHours;
import com.example.pos10.service.ifs.OperatingHoursService;
import com.example.pos10.vo.OperatingHoursReq;
import com.example.pos10.vo.OperatingHoursRes;

@RestController
public class OperatingHoursController {

    @Autowired
    private OperatingHoursService operatingHoursService;

    // 1. 批量新增或更新營業時間
    @PostMapping("/operatingHours/saveOperatingHours")
    public List<OperatingHoursRes> saveOperatingHours(@RequestBody List<OperatingHoursReq> operatingHoursReqList) {
        return operatingHoursService.saveOperatingHours(operatingHoursReqList);
    }
    
    // 2. 計算可用的預約時間段
    @GetMapping("/operatingHours/calculateAvailableStartTimes")
    public List <LocalTime> calculateAvailableStartTimes(
    		@RequestParam ("openingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime openingTime,
    		@RequestParam ("closingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime closingTime,
            @RequestParam int diningDuration) {
        // 使用服務來計算可用的時間段
        return operatingHoursService.calculateAvailableTimeSlots(openingTime, closingTime, diningDuration);
    }
    
    // 3. 查詢店鋪的營業時間，dayOfWeek 可選
    @GetMapping("/operatingHours/getOperatingHours")
    public List<OperatingHours> getOperatingHours (@RequestParam(value = "dayOfWeek", required = false) String dayOfWeek) {
        try {
            return operatingHoursService.getOperatingHours(dayOfWeek);
        } catch (IllegalArgumentException e) {
            // 返回錯誤信息
        	throw new IllegalArgumentException("無效的星期名稱: " + dayOfWeek);
        }
    }
    
    // 4. 刪除指定的營業時間
    @DeleteMapping("/operatingHours/deleteOperatingHours")
    public OperatingHoursRes deleteOperatingHours(@RequestBody List<Integer> ids) {
        return operatingHoursService.deleteOperatingHours(ids);
    }
}