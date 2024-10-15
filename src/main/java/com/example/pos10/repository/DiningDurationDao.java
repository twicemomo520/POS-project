//package com.example.pos10.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.example.pos10.entity.BusinessHours;
//import com.example.pos10.entity.DiningDuration;
//
//public interface DiningDurationDao extends JpaRepository <DiningDuration, Integer> {
//
//	// 1. 通過 BusinessHours 查詢對應的用餐時長
//	@Query("SELECT d FROM DiningDuration d JOIN d.businessHoursList b WHERE b = :businessHours")
//	List<DiningDuration> findByBusinessHours(@Param("businessHours") BusinessHours businessHours);
//	
//    // 2. 查詢特定餐廳的一天內的用餐時長
//    @Query ("SELECT d FROM DiningDuration d JOIN d.businessHoursList b WHERE b.storeId = :storeId AND b.dayOfWeek = :dayOfWeek")
//    List <DiningDuration> findDiningDurationsByStoreAndDay (@Param ("storeId") int storeId, @Param ("dayOfWeek") String dayOfWeek);
//
////    // 3. 查詢可用的用餐時段（排除已有的預約）
////    // 在這裡你可以用 LocalTime 進行時間範圍的查詢
////    @Query("SELECT d FROM DiningDuration d JOIN d.businessHoursList b WHERE b.storeId = :storeId AND b.dayOfWeek = :dayOfWeek AND b.openingTime <= :startTime AND b.closingTime >= :endTime")
////    List<DiningDuration> findAvailableDurations(@Param("storeId") int storeId, @Param("dayOfWeek") String dayOfWeek, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
//    
//    // 4. 使用 EntityGraph 來同時加載 BusinessHours
//    @EntityGraph(attributePaths = {"businessHoursList"})
//    List<DiningDuration> findByIdIn(List<Integer> ids);
//}