package com.example.pos10.controller;

import com.example.pos10.service.ifs.BusinessHoursService;
import com.example.pos10.vo.BusinessHoursReq;
import com.example.pos10.vo.BusinessHoursRes;
import com.example.pos10.entity.BusinessHours;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BusinessHoursController {

    @Autowired
    private BusinessHoursService businessHoursService;

    // 1. 新增或更新營業時間
    @PostMapping ("/businessHours/addOrUpdateBusinessHours")
    public BusinessHoursRes addOrUpdateBusinessHours(@RequestBody BusinessHoursReq businessHoursReq) {
        System.out.println("接收到的 BusinessHoursReq: " + businessHoursReq);
        if (businessHoursReq.getDiningDuration() != null) {
            System.out.println("接收到的 DiningDuration ID: " + businessHoursReq.getDiningDuration().getId());
        } else {
            System.out.println("未接收到 DiningDuration");
        }
        return businessHoursService.addOrUpdateBusinessHours(businessHoursReq);
    }

    // 2. 查詢指定店鋪的營業時間，dayOfWeek 可選
    @GetMapping("/businessHours/getBusinessHours/{storeId}")
    public List <BusinessHours> getBusinessHours (
            @PathVariable int storeId, 
            @RequestParam (value = "dayOfWeek", required = false) String dayOfWeek) {
        return businessHoursService.getBusinessHours(storeId, dayOfWeek);
    }

    // 3. 刪除指定的營業時間
    @DeleteMapping ("/businessHours/deleteBusinessHours/{id}")
    public BusinessHoursRes deleteBusinessHours (@PathVariable int id) {
        return businessHoursService.deleteBusinessHours(id);
    }
    
    // 4. 查詢所有營業時間
    @GetMapping("/businessHours/getAllBusinessHours")
    public List <BusinessHours> getAllBusinessHours () {
        // 呼叫 service 層來取得所有的營業時間
        return businessHoursService.getAllBusinessHours();
    }
}