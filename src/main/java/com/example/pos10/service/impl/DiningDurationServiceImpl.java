package com.example.pos10.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.BusinessHours;
import com.example.pos10.entity.DiningDuration;
import com.example.pos10.repository.BusinessHoursDao;
import com.example.pos10.repository.DiningDurationDao;
import com.example.pos10.service.ifs.DiningDurationService;
import com.example.pos10.vo.DiningDurationReq;
import com.example.pos10.vo.DiningDurationRes;


@Service
public class DiningDurationServiceImpl implements DiningDurationService {

	@Autowired
	private DiningDurationDao diningDurationDao;

	@Autowired
	private BusinessHoursDao businessHoursDao;

//    @Autowired
//    private ReservationManagementDao reservationManagementDao;

	// 1. 新增或更新用餐時間
	@Override
	public DiningDurationRes addOrUpdateDiningDuration(DiningDurationReq diningDurationReq) {

	    // 1. 檢查必要欄位是否為空
	    if (diningDurationReq == null || diningDurationReq.getBusinessHoursIds() == null
	            || diningDurationReq.getBusinessHoursIds().isEmpty()) {
	        return new DiningDurationRes(ResMessage.NULL_OR_EMPTY_DINING_DURATION.getCode(),
	                ResMessage.NULL_OR_EMPTY_DINING_DURATION.getMessage());
	    }

	    // 2. 加載所有的 BusinessHours，並確認是否存在
	    Set<BusinessHours> businessHoursSet = new HashSet<>(
	            businessHoursDao.findAllById(diningDurationReq.getBusinessHoursIds()));
	    if (businessHoursSet.size() != diningDurationReq.getBusinessHoursIds().size()) {
	        return new DiningDurationRes(ResMessage.BUSINESS_HOURS_NOT_FOUND_FOR_DINING_DURATION.getCode(),
	                ResMessage.BUSINESS_HOURS_NOT_FOUND_FOR_DINING_DURATION.getMessage());
	    }

	 // 3. ID驗證：如果存在 ID，進行更新；若無，則創建新記錄
	    DiningDuration diningDuration;
	    if (diningDurationReq.getId() > 0) {
	        Optional<DiningDuration> existingDuration = diningDurationDao.findById(diningDurationReq.getId());
	        if (!existingDuration.isPresent()) {
	            return new DiningDurationRes(ResMessage.DINING_DURATION_NOT_FOUND_FOR_UPDATE.getCode(),
	                    ResMessage.DINING_DURATION_NOT_FOUND_FOR_UPDATE.getMessage());
	        }
	        diningDuration = existingDuration.get();
	        diningDuration.setDurationMinutes(diningDurationReq.getDurationMinutes());
	        
	        // 初始化 businessHoursList 如果是 null
	        if (diningDuration.getBusinessHoursList() == null) {
	            diningDuration.setBusinessHoursList(new ArrayList<>());
	        }
	    } else {
	        diningDuration = new DiningDuration();
	        diningDuration.setDurationMinutes(diningDurationReq.getDurationMinutes());
	        diningDuration.setBusinessHoursList(new ArrayList<>()); // 新增初始化
	    }

//	    // 4. 檢查是否已有預訂
//	    for (BusinessHours businessHours : businessHoursSet) {
//	        if (reservationManagementDao.existsReservationInTimeRange(businessHours.getStoreId(),
//	                businessHours.getDayOfWeek(), businessHours.getOpeningTime(), businessHours.getClosingTime())) {
//	            return new DiningDurationRes(ResMessage.RESERVATION_EXISTS_IN_TIME_RANGE.getCode(),
//	                    ResMessage.RESERVATION_EXISTS_IN_TIME_RANGE.getMessage());
//	        }
//	    }

	    // 5. 檢查用餐時間是否超過營業時間
	    for (BusinessHours businessHours : businessHoursSet) {
	        LocalTime closingTimeWithBuffer = businessHours.getClosingTime()
	                .minusMinutes(diningDurationReq.getDurationMinutes());
	        if (closingTimeWithBuffer.isBefore(businessHours.getOpeningTime())) {
	            return new DiningDurationRes(ResMessage.DINING_DURATION_EXCEEDS_BUSINESS_HOURS.getCode(),
	                    ResMessage.DINING_DURATION_EXCEEDS_BUSINESS_HOURS.getMessage());
	        }
	    }

	    // 6. 清空原有的關聯並添加新的關聯
	    diningDuration.getBusinessHoursList().clear(); // 清空原有的 businessHoursList
	    diningDuration.getBusinessHoursList().addAll(businessHoursSet); // 添加新的關聯
	    
	    // 7. 更新並保存用餐時長
	    DiningDuration savedDiningDuration = diningDurationDao.save(diningDuration); // 保存用餐時長及其關聯的營業時間

	    // 7. 返回包含 diningDuration ID 的成功訊息
	    DiningDurationRes response = new DiningDurationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	    response.setDiningDurations(Collections.singletonList(savedDiningDuration));  // 將單個物件包裝成 List
	    return response;
	    
	}
	
	// 2. 查詢指定店鋪在某一天的用餐時間
	@Override
	public DiningDurationRes getDiningDurations(int storeId, String dayOfWeek) {

	    // 1. 檢查 storeId 是否有效
	    if (storeId <= 0) {
	        return new DiningDurationRes(ResMessage.INVALID_STORE_ID.getCode(),
	                ResMessage.INVALID_STORE_ID.getMessage());
	    }

	    // 2. 檢查 dayOfWeek 是否有效
	    if (dayOfWeek == null || dayOfWeek.isEmpty()) {
	        return new DiningDurationRes(ResMessage.INVALID_DAY_OF_WEEK.getCode(),
	                ResMessage.INVALID_DAY_OF_WEEK.getMessage());
	    }

	    // 3. 查詢指定店鋪在指定星期的營業時間
	    List<BusinessHours> businessHoursList = businessHoursDao.findByStoreIdAndDayOfWeek(storeId, dayOfWeek);

	    if (businessHoursList.isEmpty()) {
	        return new DiningDurationRes(ResMessage.DINING_DURATION_NOT_FOUND.getCode(),
	                ResMessage.DINING_DURATION_NOT_FOUND.getMessage());
	    }

	    // 4. 從 BusinessHours 中提取對應的 DiningDuration
	    Set<DiningDuration> diningDurations = new HashSet<>();
	    for (BusinessHours businessHours : businessHoursList) {
	        if (businessHours.getDiningDuration() != null) {
	            diningDurations.add(businessHours.getDiningDuration());
	        }
	    }

	    // 5. 檢查是否有對應的用餐時段
	    if (diningDurations.isEmpty()) {
	        return new DiningDurationRes(ResMessage.DINING_DURATION_NOT_FOUND.getCode(),
	                ResMessage.DINING_DURATION_NOT_FOUND.getMessage());
	    }

	    // 6. 返回結果
	    return new DiningDurationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),
	            new ArrayList<>(diningDurations));
	}

	// 3. 刪除指定的用餐時間
	@Override
	public DiningDurationRes deleteDiningDurations(List<Integer> ids) {
	    // 1. 檢查 ids 是否為空
	    if (ids == null || ids.isEmpty()) {
	        return new DiningDurationRes(ResMessage.INVALID_DINING_DURATION_ID.getCode(), ResMessage.INVALID_DINING_DURATION_ID.getMessage());
	    }

	    // 2. 根據 ids 查詢所有對應的用餐時段
	    List<DiningDuration> durations = diningDurationDao.findByIdIn(ids);
	    List<Integer> notFoundIds = new ArrayList<>(ids); // 初始化為傳入的 ID 列表

	    if (durations.isEmpty()) {
	        return new DiningDurationRes(ResMessage.DINING_DURATION_NOT_FOUND.getCode(), ResMessage.DINING_DURATION_NOT_FOUND.getMessage());
	    }

//	    // 3. 移除存在的 ID，並檢查是否有預訂
//	    for (DiningDuration duration : durations) {
//	        notFoundIds.remove(duration.getId());
//	        
//	        // 檢查是否已有預訂
//	        boolean hasReservations = reservationManagementDao.existsReservationInDiningDuration(duration.getId());
//	        if (hasReservations) {
//	            return new DiningDurationRes(ResMessage.DINING_DURATION_HAS_RESERVATIONS.getCode(), ResMessage.DINING_DURATION_HAS_RESERVATIONS.getMessage());
//	        }
//	    }

	    // 4. 刪除用餐時間記錄
	    diningDurationDao.deleteAll(durations);

	    return new DiningDurationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	// 4. 查詢指定時間範圍內可用的用餐時間
	@Override
	public DiningDurationRes getAvailableDiningDurations(int storeId, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
	    // 1. 檢查 storeId 是否有效
	    if (storeId <= 0) {
	        return new DiningDurationRes(ResMessage.INVALID_STORE_ID.getCode(), ResMessage.INVALID_STORE_ID.getMessage());
	    }

	    // 2. 檢查 dayOfWeek 是否有效
	    if (dayOfWeek == null || dayOfWeek.isEmpty()) {
	        return new DiningDurationRes(ResMessage.INVALID_DAY_OF_WEEK.getCode(), ResMessage.INVALID_DAY_OF_WEEK.getMessage());
	    }

	    // 3. 檢查 startTime 和 endTime 是否為 null，並且確認 startTime 是否早於 endTime
	    if (startTime == null || endTime == null) {
	        return new DiningDurationRes(ResMessage.INVALID_TIME_FORMAT.getCode(), ResMessage.INVALID_TIME_FORMAT.getMessage());
	    }

	    if (startTime.compareTo(endTime) >= 0) {
	        return new DiningDurationRes(ResMessage.INVALID_DINING_DURATION.getCode(), ResMessage.INVALID_DINING_DURATION.getMessage());
	    }

	    // 以下邏輯暫時註解掉，之後再啟用
	    /*
	    // 4. 檢查指定時間範圍內是否已有預約
	    List<DiningDuration> reservedDurations = reservationManagementDao.findReservationsInTimeRange(
	        storeId, dayOfWeek, startTime, endTime);

	    // 5. 如果時間段內已有預約，則無法使用
	    if (!reservedDurations.isEmpty()) {
	        return new DiningDurationRes(ResMessage.NO_AVAILABLE_DINING_DURATION.getCode(),
	                ResMessage.NO_AVAILABLE_DINING_DURATION.getMessage());
	    }

	    // 6. 從資料庫查詢指定的用餐時段
	    List<DiningDuration> availableDurations = diningDurationDao.findAvailableDurations(
	        storeId, dayOfWeek, startTime, endTime);

	    // 7. 如果查詢結果為空，返回無可用時間段
	    if (availableDurations.isEmpty()) {
	        return new DiningDurationRes(ResMessage.NO_AVAILABLE_DINING_DURATION.getCode(),
	                ResMessage.NO_AVAILABLE_DINING_DURATION.getMessage());
	    }

	    // 8. 返回可用的用餐時間段
	    return new DiningDurationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), availableDurations);
	    */

	    // 當暫時不需要進行上述檢查時，先返回一個暫時成功的結果，或返回空結果
	    return new DiningDurationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), new ArrayList<>());
	}
	
	// 5. 查詢所有的用餐時間
	@Override
	public List <DiningDuration> getAllDiningDurations() {
	    // 1. 從 DAO 查詢所有的用餐時間
	    List<DiningDuration> allDiningDurations = diningDurationDao.findAll();
	    
	    // 2. 檢查是否有用餐時間資料
	    if (allDiningDurations.isEmpty()) {
	        throw new IllegalArgumentException("目前沒有任何用餐時間資料");
	    }

	    // 3. 返回查詢到的所有用餐時間
	    return allDiningDurations;
	}
}