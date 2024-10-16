package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.WaitlistService;
import com.example.pos10.vo.WaitlistReq;
import com.example.pos10.vo.WaitlistRes;

@RestController
public class WaitlistController {

    @Autowired
    private WaitlistService waitlistService;

    // 1. 註冊候位
    @PostMapping("/waitlist/registerWaitlist")
    public WaitlistRes registerWaitlist(@RequestBody WaitlistReq waitlistReq) {
        return waitlistService.registerWaitlist(waitlistReq);
    }
    
    // 2. 根據顧客電話號碼查詢候位
    @GetMapping("/waitlist/findWaitlistByPhoneNumber")
    public WaitlistRes findWaitlistByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return waitlistService.findWaitlistByPhoneNumber(phoneNumber);
    }

    // 3. 查詢所有候位
    @GetMapping("/waitlist/findAllWaitlist")
    public WaitlistRes findAllWaitlists() {
        return waitlistService.findAllWaitlists();
    }

    // 4. 取消候位
    @DeleteMapping("/waitlist/cancelWaitlist")
    public WaitlistRes cancelWaitlist(@RequestParam("waitlistId") int waitlistId) {
        return waitlistService.cancelWaitlist(waitlistId);
    }

    // 5. 手動報到
    @PostMapping("/waitlist/manualCheckIn")
    public WaitlistRes manualCheckIn(@RequestParam("waitlistId") int waitlistId) {
        return waitlistService.manualCheckIn(waitlistId);
    }

    // 6. 自動通知顧客
    @PostMapping("/waitlist/sendNotificationsForAvailableTables")
    public void sendNotificationsForAvailableTables() {
        waitlistService.sendNotificationsForAvailableTables();
    }
    
    // 7. 獲取最大候位順序
    @GetMapping("/waitlist/getMaxWaitlistOrder")
    public int getMaxWaitlistOrder() {
        return waitlistService.getMaxWaitlistOrder();
    }
}