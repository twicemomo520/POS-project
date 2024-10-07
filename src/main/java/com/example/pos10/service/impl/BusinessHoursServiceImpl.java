package com.example.pos10.service.impl;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.BusinessHours;
import com.example.pos10.entity.DiningDuration;
import com.example.pos10.repository.BusinessHoursDao;
import com.example.pos10.repository.DiningDurationDao;
import com.example.pos10.service.ifs.BusinessHoursService;
import com.example.pos10.vo.BusinessHoursReq;
import com.example.pos10.vo.BusinessHoursRes;

@Service
public class BusinessHoursServiceImpl implements BusinessHoursService {

    @Autowired
    private BusinessHoursDao businessHoursDao;
    
    @Autowired
   	private DiningDurationDao diningDurationDao;

    // 1. 新增或更新營業時間
@Override
public BusinessHoursRes addOrUpdateBusinessHours(BusinessHoursReq businessHoursReq) {
    // 1. 檢查營業時間是否為空
    if (businessHoursReq.getOpeningTime() == null || businessHoursReq.getClosingTime() == null) {
        return new BusinessHoursRes(ResMessage.INVALID_OPENING_AND_CLOSING_TIME.getCode(), 
                                     "營業時間或關閉時間不能為空");
    }

    // 2. 檢查是否是跨天的營業時間
    if (!isValidBusinessHours(businessHoursReq.getOpeningTime(), businessHoursReq.getClosingTime())) {
        return new BusinessHoursRes(ResMessage.INVALID_OPENING_AND_CLOSING_TIME.getCode(), 
                                     ResMessage.INVALID_OPENING_AND_CLOSING_TIME.getMessage());
    }

    // 3. 檢查是否有重疊的時間段
    List<BusinessHours> conflictingHours = businessHoursDao.findConflictingBusinessHours(
        businessHoursReq.getStoreId(), 
        businessHoursReq.getDayOfWeek(), 
        businessHoursReq.getOpeningTime(), 
        businessHoursReq.getClosingTime()
    );

    if (businessHoursReq.getId() != 0) {
        conflictingHours = conflictingHours.stream()
            .filter(hours -> hours.getId() != businessHoursReq.getId())
            .collect(Collectors.toList());
    }

    if (!conflictingHours.isEmpty()) {
        return new BusinessHoursRes(ResMessage.CONFLICTING_BUSINESS_HOURS.getCode(), 
                                    ResMessage.CONFLICTING_BUSINESS_HOURS.getMessage());
    }

    // 4. 如果是更新操作，檢查該營業時間是否存在
    Optional<BusinessHours> existingHours = Optional.empty();
    if (businessHoursReq.getId() != 0) {
        existingHours = businessHoursDao.findById(businessHoursReq.getId());
        if (!existingHours.isPresent()) {
            return new BusinessHoursRes(ResMessage.BUSINESS_HOURS_TO_UPDATE_NOT_FOUND.getCode(), 
                                        ResMessage.BUSINESS_HOURS_TO_UPDATE_NOT_FOUND.getMessage());
        }
    }

    // 5. 執行新增或更新操作
    BusinessHours businessHours;
    if (businessHoursReq.getId() != 0) {
        businessHours = existingHours.get(); // 更新操作
        businessHours.setDayOfWeek(businessHoursReq.getDayOfWeek());
        businessHours.setOpeningTime(businessHoursReq.getOpeningTime());
        businessHours.setClosingTime(businessHoursReq.getClosingTime());
    } else {
        // 新增操作
        businessHours = new BusinessHours(
            businessHoursReq.getDayOfWeek(),
            businessHoursReq.getOpeningTime(),
            businessHoursReq.getClosingTime()
        );
    }

    // 6. 設置 DiningDuration (檢查是否為 null)
    if (businessHoursReq.getDiningDuration() != null && businessHoursReq.getDiningDuration().getId() > 0) {
        System.out.println("嘗試設置 DiningDuration ID: " + businessHoursReq.getDiningDuration().getId());
        DiningDuration diningDuration = diningDurationDao.findById(businessHoursReq.getDiningDuration().getId())
            .orElseThrow(() -> new IllegalArgumentException("無效的用餐時間 ID"));
        businessHours.setDiningDuration(diningDuration); // 設置用餐時間
        System.out.println("DiningDuration 已設置: " + diningDuration.getId());
    } else {
        System.out.println("未設置 DiningDuration");
    }
    
    // 7. 保存或更新營業時間
    BusinessHours savedBusinessHours = businessHoursDao.save(businessHours);

 // 確認保存後的 BusinessHours 和 DiningDuration
    System.out.println("已更新的 BusinessHours ID: " + savedBusinessHours.getId());
    System.out.println("已更新的 DiningDuration ID: " + (savedBusinessHours.getDiningDuration() != null ? savedBusinessHours.getDiningDuration().getId() : "NULL"));
    // 8. 返回包含儲存後 ID 和其他屬性的 BusinessHoursRes
    BusinessHoursRes response = new BusinessHoursRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    response.setId(savedBusinessHours.getId()); // 將 ID 傳回
    response.setDayOfWeek(savedBusinessHours.getDayOfWeek()); // 返回 DayOfWeek
    response.setOpeningTime(savedBusinessHours.getOpeningTime()); // 返回 OpeningTime
    response.setClosingTime(savedBusinessHours.getClosingTime()); // 返回 ClosingTime

    return response;
}
    // 驗證開店和關店時間的輔助方法
    private boolean isValidBusinessHours (LocalTime openingTime, LocalTime closingTime) {
        // 如果開店時間晚於關店時間，表示跨天營業
        if (openingTime.isAfter(closingTime)) {
            return true; // 跨天營業是允許的
        }

        // 非跨天情況下，開店時間必須早於關店時間
        return !openingTime.equals(closingTime);
    }

    // 2. 查詢指定店鋪的營業時間，dayOfWeek 可以選擇性地指定
    @Override
    public List<BusinessHours> getBusinessHours(int storeId, String dayOfWeek) {
        if (dayOfWeek == null || dayOfWeek.isEmpty()) {
            // 查詢該店鋪的所有營業時間
            return businessHoursDao.findAllBusinessHoursByStore(storeId);
        } else {
            // 查詢該店鋪指定日期的營業時間
            return businessHoursDao.findBusinessHoursByDayAndStore(storeId, dayOfWeek);
        }
    }

    // 3. 刪除指定的營業時間
    @Override
    public BusinessHoursRes deleteBusinessHours(int id) {
        // 檢查該營業時間是否存在
        if (!businessHoursDao.existsById(id)) {
            return new BusinessHoursRes(ResMessage.BUSINESS_HOURS_NOT_FOUND_FOR_DELETION.getCode(), //
            		ResMessage.BUSINESS_HOURS_NOT_FOUND_FOR_DELETION.getMessage());
        }

        // 刪除營業時間
        businessHoursDao.deleteById(id);
        return new BusinessHoursRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }
    
    // 查詢所有營業時間
    @Override
    public List <BusinessHours> getAllBusinessHours() {
        // 1. 從 DAO 查詢所有營業時間
        List <BusinessHours> allBusinessHours = businessHoursDao.findAll();
        
        // 2. 檢查是否有營業時間資料
        if (allBusinessHours.isEmpty()) {
            throw new IllegalArgumentException("目前沒有任何營業時間資料");
        }

        // 3. 返回查詢到的所有營業時間
        return allBusinessHours;
    }
}