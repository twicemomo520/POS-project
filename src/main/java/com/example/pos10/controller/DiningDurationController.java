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

import com.example.pos10.entity.DiningDuration;
import com.example.pos10.service.ifs.DiningDurationService;
import com.example.pos10.vo.DiningDurationReq;
import com.example.pos10.vo.DiningDurationRes;


@RestController
public class DiningDurationController {

    @Autowired
    private DiningDurationService diningDurationService;

    // 1. 新增或更新用餐時間
    @PostMapping("/diningDuration/addOrUpdateDiningDuration")
    public DiningDurationRes addOrUpdateDiningDuration (@RequestBody DiningDurationReq diningDurationReq) {
        System.out.println("接收到的 businessHoursIds: " + diningDurationReq.getBusinessHoursIds());
        System.out.println("接收到的 durationMinutes: " + diningDurationReq.getDurationMinutes());
        return diningDurationService.addOrUpdateDiningDuration(diningDurationReq);
    }

    // 2. 查詢指定店鋪在某一天的用餐時間
    @GetMapping("/diningDuration/getDiningDurations")
    public DiningDurationRes getDiningDurations (@RequestParam int storeId, @RequestParam String dayOfWeek) {
        return diningDurationService.getDiningDurations(storeId, dayOfWeek);
    }
    
    // 3. 刪除指定的用餐時間
    @DeleteMapping("/diningDuration/deleteDiningDurations")
    public DiningDurationRes deleteDiningDurations (@RequestBody List<Integer> ids) {
        return diningDurationService.deleteDiningDurations(ids);
    }

    // 4. 查詢可用的用餐時段，排除已有的預約
    @GetMapping("/diningDuration/getAvailableDiningDurations")
    public DiningDurationRes getAvailableDiningDurations (
            @RequestParam int storeId,
            @RequestParam String dayOfWeek,
            @RequestParam ("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam ("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        return diningDurationService.getAvailableDiningDurations(storeId, dayOfWeek, startTime, endTime);
    }
    
    // 5. 查詢所有的用餐時間
    @GetMapping("/diningDuration/getAllDiningDurations")
    public List<DiningDuration> getAllDiningDurations() {
        return diningDurationService.getAllDiningDurations();
    }
}