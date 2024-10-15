package com.example.pos10.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pos10.entity.OperatingHours;

public interface OperatingHoursDao extends JpaRepository<OperatingHours, Integer> {

	// 1. 查詢所有營業時間，dayOfWeek 可選
	@Query("SELECT o FROM OperatingHours o WHERE (:dayOfWeek IS NULL OR o.dayOfWeek = :dayOfWeek) ORDER BY o.dayOfWeek")
	List<OperatingHours> findOperatingHours(@Param("dayOfWeek") OperatingHours.DayOfWeek dayOfWeek);

	// 2. 查詢指定的營業時間段是否與已有時間衝突，並根據 dayOfWeek 過濾
	@Query("SELECT o FROM OperatingHours o WHERE (:dayOfWeek IS NULL OR o.dayOfWeek = :dayOfWeek) " +
		       "AND (o.openingTime < :closingTime AND o.closingTime > :openingTime)")
	List<OperatingHours> findConflictingOperatingHours(
		    @Param("dayOfWeek") OperatingHours.DayOfWeek dayOfWeek, // 改成 OperatingHours.DayOfWeek
		    @Param("openingTime") LocalTime openingTime,
		    @Param("closingTime") LocalTime closingTime
		);
	
	// 3. 根據當前日期獲取對應的開放時間和關閉時間（應用在TableManagement）
	@Query("SELECT o.openingTime, o.closingTime, o.diningDuration FROM OperatingHours o WHERE o.dayOfWeek = :dayOfWeek")
	public List<Object[]> getOperatingHours(@Param("dayOfWeek") OperatingHours.DayOfWeek dayOfWeek);
}