package com.example.pos10.repository;

import com.example.pos10.entity.BusinessHours;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BusinessHoursDao extends JpaRepository <BusinessHours, Integer> {

    // 1. 根據 storeId (店家 ID) 和 dayOfWeek (星期幾) 查詢所有營業時間段
    @Query ("SELECT b FROM BusinessHours b WHERE b.storeId = :storeId AND b.dayOfWeek = :dayOfWeek")
    List <BusinessHours> findBusinessHoursByDayAndStore (@Param ("storeId") int storeId, @Param ("dayOfWeek") String dayOfWeek);

    // 2. 查詢特定餐廳的所有營業時間
    @Query ("SELECT b FROM BusinessHours b WHERE b.storeId = :storeId")
    List <BusinessHours> findAllBusinessHoursByStore (@Param ("storeId") int storeId);

	 // 3. 查詢指定的營業時間段是否與已有時間衝突
	    @Query("SELECT b FROM BusinessHours b WHERE b.storeId = :storeId AND b.dayOfWeek = :dayOfWeek " +
	           "AND ((b.openingTime < :closingTime AND b.closingTime > :openingTime))")
	    List<BusinessHours> findConflictingBusinessHours(
	        @Param("storeId") int storeId, 
	        @Param("dayOfWeek") String dayOfWeek, 
	        @Param("openingTime") LocalTime openingTime, 
	        @Param("closingTime") LocalTime closingTime
	    );
    
    // 4. 根據 storeId 和 dayOfWeek 查詢對應的營業時間
    List <BusinessHours> findByStoreIdAndDayOfWeek (int storeId, String dayOfWeek);
}