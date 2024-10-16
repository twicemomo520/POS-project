package com.example.pos10.service.ifs;

import com.example.pos10.vo.WaitlistReq;
import com.example.pos10.vo.WaitlistRes;

public interface WaitlistService {
    
    // 1. 儲存候位
    public WaitlistRes registerWaitlist(WaitlistReq waitlistReq);
    
    // 2. 根據顧客電話號碼查詢候位
    public WaitlistRes findWaitlistByPhoneNumber(String phoneNumber);
    
    // 3. 查詢所有候位
    public WaitlistRes findAllWaitlists();

    // 4. 取消候位
    public WaitlistRes cancelWaitlist(int waitlistId);

    // 5. 手動報到更新候位狀態
    public WaitlistRes manualCheckIn(int waitlistId);

    // 6. 自動通知顧客
    public void sendNotificationsForAvailableTables();
    
	// 7. 獲取最大候位順序
    public int getMaxWaitlistOrder();
}