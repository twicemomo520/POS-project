package com.example.pos10.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.OperatingHours;
import com.example.pos10.repository.OperatingHoursDao;
import com.example.pos10.service.ifs.OperatingHoursService;
import com.example.pos10.vo.OperatingHoursReq;
import com.example.pos10.vo.OperatingHoursRes;

@Service
public class OperatingHoursServiceImpl implements OperatingHoursService {

    @Autowired
    private OperatingHoursDao operatingHoursDao;

    // 1. 新增或更新營業時間（單筆或批量）
    @Override
    public List<OperatingHoursRes> saveOperatingHours(List<OperatingHoursReq> operatingHoursReqList) {
        List<OperatingHoursRes> responses = new ArrayList<>();

        for (OperatingHoursReq req : operatingHoursReqList) {
        	// 1. 驗證營業時間：檢查營業時間是否有效
        	if (req.getOpeningTime() == null || req.getClosingTime() == null) {
        	    responses.add(new OperatingHoursRes(ResMessage.INVALID_OPERATING_HOURS.getCode(), //
        	    		ResMessage.INVALID_OPERATING_HOURS.getMessage()));
        	    return responses; // 返回錯誤響應，並停止處理
        	}
        	if (req.getOpeningTime().isAfter(req.getClosingTime())) {
        	    responses.add(new OperatingHoursRes(ResMessage.OPENING_TIME_AFTER_CLOSING.getCode(), //
        	    		ResMessage.OPENING_TIME_AFTER_CLOSING.getMessage()));
        	    return responses; // 返回錯誤響應，並停止處理
        	}

        	// 2. 驗證用餐時間：檢查用餐時間是否有效
        	if (req.getDiningDuration() <= 0) {
        	    responses.add(new OperatingHoursRes(ResMessage.INVALID_DINING_DURATION.getCode(), //
        	    		ResMessage.INVALID_DINING_DURATION.getMessage()));
        	    return responses; // 返回錯誤響應，並停止處理
        	}

        	// 3. 檢查營業時間是否與已有的時間段衝突
        	List<OperatingHours> conflictingHours = operatingHoursDao.findConflictingOperatingHours(
        	    req.getDayOfWeek(), req.getOpeningTime(), req.getClosingTime());
        	if (!conflictingHours.isEmpty()) {
        	    responses.add(new OperatingHoursRes(ResMessage.CONFLICTING_OPERATING_HOURS.getCode(), //
        	    		ResMessage.CONFLICTING_OPERATING_HOURS.getMessage()));
        	    return responses; // 返回錯誤響應，並停止處理
        	}

            // 4. 新增營業時間邏輯
            OperatingHours operatingHours = new OperatingHours();
            operatingHours.setDayOfWeek(req.getDayOfWeek());
            operatingHours.setOpeningTime(req.getOpeningTime());
            operatingHours.setClosingTime(req.getClosingTime());
            operatingHours.setDiningDuration(req.getDiningDuration()); // 設置用餐時間

            // 5. 保存營業時間
            operatingHoursDao.save(operatingHours);
            responses.add(new OperatingHoursRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage()));
        }

        return responses; // 返回所有的響應
    }

	
    // 2. 計算可用的預約時間段
    @Override
    public List<LocalTime> calculateAvailableTimeSlots(LocalTime openingTime, LocalTime closingTime, int diningDuration) {
        List<LocalTime> timeSlots = new ArrayList<>();
        int breakDuration = 5; // 固定的間隔時間 5 分鐘

        // 1. 驗證輸入參數
        if (openingTime == null || closingTime == null) {
            throw new IllegalArgumentException("開放時間和結束時間不能為空");
        }

        if (diningDuration <= 0) {
            throw new IllegalArgumentException("用餐時間必須大於零");
        }

        if (openingTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("開放時間必須早於結束時間");
        }

        // 2. 計算可用的時間段
        LocalTime slotTime = openingTime;

        while (slotTime.plusMinutes(diningDuration).isBefore(closingTime) || 
               slotTime.plusMinutes(diningDuration).equals(closingTime)) {
            // 將可用的開始時間添加到時間段列表中
            timeSlots.add(slotTime);

            // 更新開始時間，加上用餐時間及固定的 5 分鐘間隔
            slotTime = slotTime.plusMinutes(diningDuration + breakDuration);
        }

        return timeSlots;
    }
    
    // 3. 查詢店鋪的營業時間，dayOfWeek 可選
    @Override
    public List<OperatingHours> getOperatingHours(String dayOfWeek) {
        OperatingHours.DayOfWeek dayOfWeekEnum = null;

        // 檢查 dayOfWeek 是否為 null 或空字符串
        if (dayOfWeek == null || dayOfWeek.isEmpty()) {
            // 如果是空的，直接返回所有營業時間
            return operatingHoursDao.findOperatingHours(null);
        }

        // 嘗試將 dayOfWeek 轉換為枚舉
        try {
            dayOfWeekEnum = OperatingHours.DayOfWeek.valueOf(dayOfWeek.toUpperCase()); // 確保大寫以匹配枚舉
        } catch (IllegalArgumentException e) {
            // 拋出異常，如果無法轉換
            throw new IllegalArgumentException("無效的星期名稱: " + dayOfWeek);
        }

        // 查詢營業時間
        return operatingHoursDao.findOperatingHours(dayOfWeekEnum);
    }
	
	// 4. 刪除指定的營業時間
    @Override
    public OperatingHoursRes deleteOperatingHours(List<Integer> ids) {
        // 檢查傳入的 ID 列表是否為空
        if (ids == null || ids.isEmpty()) {
            return new OperatingHoursRes(ResMessage.INVALID_ID_LIST.getCode(), ResMessage.INVALID_ID_LIST.getMessage());
        }

        // 建立一個響應結果
        OperatingHoursRes response = new OperatingHoursRes();
        List<Integer> deletedIds = new ArrayList<>(); // 儲存已刪除的 ID
        List<Integer> notFoundIds = new ArrayList<>(); // 儲存未找到的 ID

        // 遍歷每個 ID 進行刪除
        for (Integer id : ids) {
            Optional<OperatingHours> operatingHour = operatingHoursDao.findById(id);
            if (operatingHour.isPresent()) {
                operatingHoursDao.deleteById(id);
                deletedIds.add(id);
            } else {
                notFoundIds.add(id);
            }
        }

        // 設定響應結果
        response.setDeletedIds(deletedIds);
        if (!notFoundIds.isEmpty()) {
            response.setMessage("部分 ID 未找到，未能刪除: " + notFoundIds);
        } else {
            response.setMessage("所有指定的營業時間已成功刪除");
        }

        return response;
    }
}