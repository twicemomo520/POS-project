package com.example.pos10.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pos10.entity.OperatingHours;

public interface OperatingHoursDao extends JpaRepository<OperatingHours, Integer> {

	// 1. 檢查指定的 dayOfWeek、openingTime 和 closingTime 是否已存在
	@Query("SELECT COUNT(oh) > 0 FROM OperatingHours oh WHERE oh.dayOfWeek = :dayOfWeek AND " +
		       "(:openingTime < oh.closingTime AND :closingTime > oh.openingTime)")
		boolean existsOverlappingOperatingHours(
		    @Param("dayOfWeek") OperatingHours.DayOfWeek dayOfWeek, 
		    @Param("openingTime") LocalTime openingTime, 
		    @Param("closingTime") LocalTime closingTime);
	
	// 2. 查詢所有營業時間，dayOfWeek 可選
	@Query("SELECT o FROM OperatingHours o WHERE (:dayOfWeek IS NULL OR o.dayOfWeek = :dayOfWeek) ORDER BY o.dayOfWeek")
	List<OperatingHours> findOperatingHours(@Param("dayOfWeek") OperatingHours.DayOfWeek dayOfWeek);

}