package com.example.pos10.service.ifs;

import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.DiningDuration;
import com.example.pos10.vo.DiningDurationReq;
import com.example.pos10.vo.DiningDurationRes;

public interface DiningDurationService {

    // 1. 新增或更新用餐時間
	public DiningDurationRes addOrUpdateDiningDuration (DiningDurationReq diningDurationReq);

    // 2. 查詢指定店鋪在某一天的用餐時間
	public DiningDurationRes getDiningDurations (int storeId, String dayOfWeek);

    // 3. 刪除指定的用餐時間
	public DiningDurationRes deleteDiningDurations (List <Integer> ids);

    // 4. 查詢可用的用餐時段，排除已有的預約
	public DiningDurationRes getAvailableDiningDurations(int storeId, String dayOfWeek, LocalTime startTime, LocalTime endTime);

	// 5,查詢所有的用餐時間
	public List <DiningDuration> getAllDiningDurations ();
}