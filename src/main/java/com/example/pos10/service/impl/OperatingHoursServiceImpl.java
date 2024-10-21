package com.example.pos10.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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
    @Transactional
    public List<OperatingHoursRes> saveOperatingHours(List<OperatingHoursReq> operatingHoursReqList) {
        List<OperatingHoursRes> responses = new ArrayList<>();

        for (OperatingHoursReq req : operatingHoursReqList) {
            // 1. 驗證營業時間：檢查營業時間是否有效
            if (req.getOpeningTime() == null || req.getClosingTime() == null) {
                responses.add(new OperatingHoursRes(400, "營業開始時間或結束時間不得為空 !!!"));
                return responses;
            }
            if (req.getOpeningTime().isAfter(req.getClosingTime())) {
                responses.add(new OperatingHoursRes(400, "營業開始時間不能晚於結束時間 !!!"));
                return responses;
            }

            // 2. 驗證用餐時間：檢查用餐時間是否有效
            if (req.getDiningDuration() <= 0) {
                responses.add(new OperatingHoursRes(400, "用餐時間必須大於 0 !!!"));
                return responses;
            }

            // 3. 驗證清潔時間：檢查清潔時間是否有效
            if (req.getCleaningBreak() != null && req.getCleaningBreak() <= 0) {
                responses.add(new OperatingHoursRes(400, "清潔時間必須大於 0 !!!"));
                return responses;
            }

            // 4. 根據大時間段自動計算細分時間段
            List<LocalTime> timeSlots = calculateAvailableTimeSlots(
                    req.getOpeningTime(), req.getClosingTime(), req.getDiningDuration(), req.getCleaningBreak());

            // 5. 將細分時間段插入 operating_hours 表
            for (LocalTime slot : timeSlots) {
                OperatingHours operatingHours = new OperatingHours();
                operatingHours.setDayOfWeek(req.getDayOfWeek());
                operatingHours.setOpeningTime(slot); // 每個時間段的開始時間
                operatingHours.setClosingTime(slot.plusMinutes(req.getDiningDuration())); // 對應的結束時間
                operatingHours.setDiningDuration(req.getDiningDuration());
                operatingHours.setCleaningBreak(req.getCleaningBreak());

                // 保存每個細分時間段
                operatingHoursDao.save(operatingHours);
            }

            responses.add(new OperatingHoursRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage()));
        }

        return responses;
    }

    // 2. 計算可用的預約時間段（有清潔時間）
    public List <LocalTime> calculateAvailableTimeSlots (LocalTime openingTime, LocalTime closingTime, int diningDuration, Integer cleaningBreak) {
        List <LocalTime> timeSlots = new ArrayList<>();

        // 如果 cleaningBreak 是 NULL，設置默認值
        int breakDuration = (cleaningBreak != null) ? cleaningBreak : 0; // 如果 cleaningBreak 是 null，則設置為 0

        // 驗證輸入參數
        if (openingTime == null || closingTime == null) {
            throw new IllegalArgumentException("開放時間和結束時間不能為空");
        }

        if (diningDuration <= 0) {
            throw new IllegalArgumentException("用餐時間必須大於零");
        }

        if (openingTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("開放時間必須早於結束時間");
        }

        LocalTime slotTime = openingTime;

        while (slotTime.plusMinutes(diningDuration).isBefore(closingTime) || 
               slotTime.plusMinutes(diningDuration).equals(closingTime)) {
            // 添加可用開始時間到時間段列表
            timeSlots.add(slotTime);

            // 更新時間，加上用餐時間及清潔時間
            slotTime = slotTime.plusMinutes(diningDuration + breakDuration);
        }

        return timeSlots;
    }    
    
    // 3. 查詢店鋪的營業時間，dayOfWeek 可選
    @Override
    public List <OperatingHours> getOperatingHours (String dayOfWeek) {
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
    @Transactional
    public OperatingHoursRes deleteOperatingHours(List <Integer> ids) {
        // 檢查傳入的 ID 列表是否為空
        if (ids == null || ids.isEmpty()) {
            return new OperatingHoursRes(400, "傳入的 營業時間 ID 列表不能為空或 null !!!");
        }

        // 建立一個響應結果
        OperatingHoursRes response = new OperatingHoursRes();
        List <Integer> deletedIds = new ArrayList<>(); // 儲存已刪除的 ID
        List <Integer> notFoundIds = new ArrayList<>(); // 儲存未找到的 ID

        // 遍歷每個 ID 進行刪除
        for (Integer id : ids) {
            Optional <OperatingHours> operatingHour = operatingHoursDao.findById(id);
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