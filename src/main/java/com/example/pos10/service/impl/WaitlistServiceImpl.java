package com.example.pos10.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.entity.Waitlist;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.repository.WaitlistDao;
import com.example.pos10.service.ifs.WaitlistService;
import com.example.pos10.vo.WaitlistReq;
import com.example.pos10.vo.WaitlistRes;

@Service
public class WaitlistServiceImpl implements WaitlistService {

    @Autowired
    private WaitlistDao waitlistDao;
    
    @Autowired
    private TableManagementDao tableManagementDao;
    
    @Autowired
    private EmailService emailService;

    // 1. 儲存候位
    @Override
    @Transactional
    public WaitlistRes registerWaitlist(WaitlistReq waitlistReq) {
        // 1. 驗證顧客姓名
        if (waitlistReq.getCustomerName() == null || waitlistReq.getCustomerName().isEmpty()) {
            return new WaitlistRes(ResMessage.INVALID_CUSTOMER_NAME.getCode(), ResMessage.INVALID_CUSTOMER_NAME.getMessage());
        }

        // 2. 驗證電話號碼格式
        String phoneNumber = waitlistReq.getCustomerPhoneNumber();
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            return new WaitlistRes(ResMessage.INVALID_PHONE_NUMBER_FORMAT.getCode(), ResMessage.INVALID_PHONE_NUMBER_FORMAT.getMessage());
        }

        // 3. 驗證電子郵件格式
        String email = waitlistReq.getCustomerEmail();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new WaitlistRes(ResMessage.INVALID_EMAIL_FORMAT.getCode(), ResMessage.INVALID_EMAIL_FORMAT.getMessage());
        }

        // 4. 驗證候位人數
        int waitListPeople = waitlistReq.getWaitListPeople();
        if (waitListPeople <= 0) {
            return new WaitlistRes(ResMessage.INVALID_WAITLIST_PEOPLE.getCode(), ResMessage.INVALID_WAITLIST_PEOPLE.getMessage());
        }

        // 5. 確認候位時間
        if (waitlistReq.getWaitTime() == null) {
            return new WaitlistRes(ResMessage.INVALID_WAIT_TIME.getCode(), ResMessage.INVALID_WAIT_TIME.getMessage());
        }

        // 6. 檢查重複候位
        List <Waitlist> existingWaitlists = waitlistDao.findByCustomerPhoneNumber(waitlistReq.getCustomerPhoneNumber());
        if (!existingWaitlists.isEmpty()) {
            return new WaitlistRes(ResMessage.DUPLICATE_WAITLIST.getCode(), ResMessage.DUPLICATE_WAITLIST.getMessage());
        }

        // 7. 獲取當前的最大 waitlist_order
        int maxOrder = waitlistDao.findMaxWaitlistOrder(); // 假設你在 WaitlistDao 中有這個方法

        // 8. 創建新的候位實體
        Waitlist waitlist = new Waitlist();
        waitlist.setCustomerName(waitlistReq.getCustomerName());
        waitlist.setCustomerPhoneNumber(waitlistReq.getCustomerPhoneNumber());
        waitlist.setCustomerEmail(waitlistReq.getCustomerEmail());
        waitlist.setCustomerGender(waitlistReq.getCustomerGender());
        waitlist.setWaitListPeople(waitlistReq.getWaitListPeople());
        waitlist.setWaitingDate(waitlistReq.getWaitingDate());
        waitlist.setWaitTime(waitlistReq.getWaitTime());
        waitlist.setWaitlistOrder(maxOrder + 1); // 設定新的候位順序

        // 9. 儲存候位資訊
        waitlistDao.save(waitlist);

        return new WaitlistRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 2. 根據顧客電話號碼查詢候位
    @Override
    public WaitlistRes findWaitlistByPhoneNumber(String phoneNumber) {
        // 1. 驗證電話號碼格式
        if (phoneNumber == null || (!phoneNumber.matches("\\d{10}") && !phoneNumber.matches("\\d{3}"))) {
            return new WaitlistRes(ResMessage.INVALID_PHONE_NUMBER_FORMAT.getCode(), ResMessage.INVALID_PHONE_NUMBER_FORMAT.getMessage());
        }

        List<Waitlist> waitlists;

        // 2. 如果輸入的是三碼，則查詢結尾為這三碼的所有電話號碼
        if (phoneNumber.length() == 3) {
            waitlists = waitlistDao.findByCustomerPhoneNumberEndingWith(phoneNumber);
        } else {
            // 3. 否則查詢完整的電話號碼
            waitlists = waitlistDao.findByCustomerPhoneNumber(phoneNumber);
        }

        // 4. 檢查查詢結果是否為空
        if (waitlists.isEmpty()) {
            return new WaitlistRes(ResMessage.NO_WAITLIST_FOUND.getCode(), ResMessage.NO_WAITLIST_FOUND.getMessage());
        }
        
        // 5. 將查詢結果轉換為 WaitlistReq 格式
        List <WaitlistReq> waitlistReqs = waitlists.stream()
            .map(waitlist -> new WaitlistReq(
                waitlist.getWaitlistId(),
                waitlist.getCustomerName(),
                waitlist.getCustomerPhoneNumber(),
                waitlist.getCustomerEmail(),
                waitlist.getCustomerGender(),
                waitlist.getWaitListPeople(),
                waitlist.getWaitingDate(),
                waitlist.getWaitTime(),
                waitlist.getWaitlistOrder()
            ))
            .collect(Collectors.toList());

        // 6. 返回成功結果與查詢到的資料
        return new WaitlistRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), waitlistReqs);
    }

    // 3. 查詢所有候位
    @Override
    public WaitlistRes findAllWaitlists() {
        List<Waitlist> waitlists = waitlistDao.findAllWaitlists();

        // 檢查查詢結果是否為空
        if (waitlists.isEmpty()) {
            return new WaitlistRes(ResMessage.NO_WAITLIST_FOUND.getCode(), ResMessage.NO_WAITLIST_FOUND.getMessage());
        }

        // 將查詢結果轉換為 WaitlistReq 格式
        List<WaitlistReq> waitlistReqs = waitlists.stream()
            .map(waitlist -> new WaitlistReq(
                waitlist.getWaitlistId(),
                waitlist.getCustomerName(),
                waitlist.getCustomerPhoneNumber(),
                waitlist.getCustomerEmail(),
                waitlist.getCustomerGender(),
                waitlist.getWaitListPeople(),
                waitlist.getWaitingDate(),
                waitlist.getWaitTime(),
                waitlist.getWaitlistOrder()
            ))
            .collect(Collectors.toList());

        // 返回成功結果與查詢到的資料
        return new WaitlistRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), waitlistReqs);
    }

    // 4. 取消候位
    @Override
    @Transactional
    public WaitlistRes cancelWaitlist(int waitlistId) {
        // 1. 透過 waitlistId 查詢對應的候位
        Waitlist waitlist = waitlistDao.findById(waitlistId).orElse(null);
        if (waitlist == null) {
            return new WaitlistRes(ResMessage.NO_WAITLIST_FOUND.getCode(), ResMessage.NO_WAITLIST_FOUND.getMessage());
        }

        // 2. 檢查候位是否已被取消
        if (waitlist.getWaitlistOrder() == 0) { // 假設 0 表示已取消
            return new WaitlistRes(ResMessage.WAITLIST_ALREADY_CANCELLED.getCode(), ResMessage.WAITLIST_ALREADY_CANCELLED.getMessage());
        }

        // 3. 執行取消操作
        waitlistDao.cancelWaitlist(waitlistId);

        // 4. 更新所有剩餘候位的順序
        List<Waitlist> remainingWaitlists = waitlistDao.findAllWaitlists();
        for (int i = 0; i < remainingWaitlists.size(); i++) {
            Waitlist remaining = remainingWaitlists.get(i);
            remaining.setWaitlistOrder(i + 1); // 更新順序
            waitlistDao.save(remaining);
        }

        // 5. 返回成功訊息
        return new WaitlistRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 5. 手動報到更新候位狀態
    @Override
    @Transactional
    public WaitlistRes manualCheckIn(int waitlistId) {
        // 1. 檢查候位是否存在
        Waitlist waitlist = waitlistDao.findById(waitlistId).orElse(null);
        if (waitlist == null) {
            return new WaitlistRes(ResMessage.NO_WAITLIST_FOUND.getCode(), ResMessage.NO_WAITLIST_FOUND.getMessage());
        }

        // 2. 檢查候位的當前狀態
        if (waitlist.getWaitlistOrder() == 0) { // 假設 0 表示已報到
            return new WaitlistRes(ResMessage.WAITLIST_ALREADY_CHECKED_IN.getCode(), ResMessage.WAITLIST_ALREADY_CHECKED_IN.getMessage());
        }

        // 3. 查找可用的桌位
        List<TableManagement> availableTables = tableManagementDao.findAvailableTablesOrderedByCapacity(); // 查詢所有狀態為 "AVAILABLE" 的桌位

        // 4. 檢查是否有可用桌位
        if (availableTables.isEmpty()) {
            return new WaitlistRes(ResMessage.NO_AVAILABLE_TABLES.getCode(), ResMessage.NO_AVAILABLE_TABLES.getMessage());
        }

        // 5. 自動分配桌位
        List<TableManagement> selectedTables = new ArrayList<>();
        int totalCapacity = 0;
        int requiredPeople = waitlist.getWaitListPeople(); // 從候位獲取人數

        // 5.1 優先選擇符合人數的桌位
        for (TableManagement table : availableTables) {
            if (table.getTableCapacity() == requiredPeople) {
                selectedTables.add(table);
                totalCapacity += table.getTableCapacity();
                break; // 找到符合人數的桌位後停止
            }
        }

        // 5.2 如果沒有符合的桌位，則選擇大於人數的桌位
        if (totalCapacity < requiredPeople) {
            for (TableManagement table : availableTables) {
                if (table.getTableCapacity() > requiredPeople) {
                    selectedTables.add(table);
                    totalCapacity += table.getTableCapacity();
                    if (totalCapacity >= requiredPeople) {
                        break; // 當總容量滿足人數需求後停止分配
                    }
                }
            }
        }

        // 5.3 如果仍然沒有滿足需求，則選擇剩餘的桌位
        if (totalCapacity < requiredPeople) {
            for (TableManagement table : availableTables) {
                selectedTables.add(table);
                totalCapacity += table.getTableCapacity();
                if (totalCapacity >= requiredPeople) {
                    break; // 當總容量滿足人數需求後停止分配
                }
            }
        }

        // 6. 更新已分配的桌位狀態為 用餐中
        try {
            selectedTables.forEach(table -> {
                tableManagementDao.updateTableStatus(table.getTableNumber(), TableManagement.TableStatus.用餐中);
            });
        } catch (Exception e) {
            return new WaitlistRes(500, "更新桌位狀態時發生錯誤");
        }

        // 7. 更新候位狀態
        waitlistDao.delete(waitlist); // 刪除報到的候位

        // 8. 返回成功訊息
        String tableNumbers = selectedTables.stream()
            .map(TableManagement::getTableNumber)
            .collect(Collectors.joining(", "));

        String message = String.format("已分配桌位%s", tableNumbers);
        return new WaitlistRes(ResMessage.SUCCESS.getCode(), message);
    }
    
    // 6. 自動通知顧客
    @Override
    @Scheduled(cron = "0 */5 * * * ?") // 每五分鐘執行一次
    public void sendNotificationsForAvailableTables() {
        System.out.println("開始發送通知...");

        List<Waitlist> waitlists = waitlistDao.findAllWaitlists();
        List<TableManagement> availableTables = tableManagementDao.findAvailableTablesOrderedByCapacity();

        int notificationCount = 0;

        if (!availableTables.isEmpty()) {
            for (Waitlist waitlist : waitlists) {
                if (waitlist.getWaitlistOrder() != 0) {
                    try {
                        emailService.sendWaitlistNotificationEmail(
                            waitlist.getCustomerEmail(),
                            waitlist.getCustomerName(),
                            waitlist.getWaitListPeople()
                        );

                        notificationCount++;
                    } catch (Exception e) {
                        System.err.println("發送郵件失敗給顧客 " + waitlist.getCustomerName() + ": " + e.getMessage());
                    }
                }
            }
        }

        System.out.println("共發送通知數量: " + notificationCount);
    }

    // 7. 獲取最大候位順序
    @Override
    public int getMaxWaitlistOrder() {
        return waitlistDao.findMaxWaitlistOrder(); // 通過 DAO 獲取最大候位順序
    }
}