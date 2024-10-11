package com.example.pos10.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Integer> {

	// 1. 查詢可用桌位 (桌位狀態為 AVAILABLE，且無重疊預訂時間)
	@Query("SELECT t FROM TableManagement t WHERE t.tableStatus = 'AVAILABLE' "
			+ "AND NOT EXISTS (SELECT r FROM Reservation r WHERE t MEMBER OF r.tables "
			+ "AND r.reservationDate = :reservationDate "
			+ "AND (:startTime < r.reservationEndingTime AND :endTime > r.reservationStartTime))")
	List<TableManagement> findAvailableTables(@Param("reservationDate") LocalDate reservationDate,
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

	// 2. 儲存訂位
	@Override
	<S extends Reservation> S save(S reservation);

    // 3. 根據顧客電話號碼查詢訂位
    @Query("SELECT r FROM Reservation r WHERE r.customerPhoneNumber = :phoneNumber")
    List<Reservation> findByCustomerPhoneNumber(@Param("phoneNumber") String phoneNumber);

    // 4. 查詢某日期的所有訂位
    @Query("SELECT r FROM Reservation r JOIN FETCH r.tables WHERE r.reservationDate = :reservationDate")
    List<Reservation> findAllByReservationDate(@Param("reservationDate") LocalDate reservationDate);
    
    // 5. 刪除與指定桌位號碼對應的訂位（取消訂位）
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.id IN " +
           "(SELECT r2.id FROM Reservation r2 JOIN r2.tables t WHERE t.tableNumber = :tableNumber)")
    void deleteByTableNumber(@Param("tableNumber") String tableNumber);
//
//    // 6. 自動更新桌位狀態與 10 分鐘保留規則整合
//    @Modifying
//    @Query("UPDATE TableManagement t " +
//           "SET t.tableStatus = CASE " +
//           "   WHEN (SELECT r.reservationStarttime FROM Reservation r JOIN r.tables tbl WHERE tbl = t AND r.reservationDate = :currentDate) <= :cutOffTime " +
//           "       AND t.tableStatus = 'RESERVED' THEN 'AVAILABLE' " +
//           "   ELSE t.tableStatus " +
//           "END " +
//           "WHERE t.tableNumber IN (SELECT tbl.tableNumber FROM Reservation r JOIN r.tables tbl WHERE r.reservationDate = :currentDate)")
//    int autoUpdateTableStatusToAvailable(@Param("currentDate") LocalDate currentDate, @Param("cutOffTime") LocalTime cutOffTime);
}