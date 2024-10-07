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
import com.example.pos10.entity.ReservationManagement;

@Repository
public interface ReservationDao extends JpaRepository <Reservation, Integer> {

    // 1. 儲存訂位
    @Override
    <S extends Reservation> S save( S reservation);

    // 2. 根據顧客電話號碼查詢訂位
    @Query ("SELECT r FROM Reservation r WHERE r.customerPhoneNumber = :phoneNumber")
    List <Reservation> findByCustomerPhoneNumber ( @Param ("phoneNumber") String phoneNumber);

    // 3. 查詢某日期的所有訂位
    @Query ("SELECT r FROM Reservation r WHERE r.reservationManagement.reservationDate = :reservationDate")
    List <Reservation> findAllByReservationDate (@Param ("reservationDate") LocalDate reservationDate);

    // 4. 取消訂位（同時將桌位狀態更新為 'AVAILABLE'）
    @Modifying
    @Query ("UPDATE TableManagement t SET t.tableStatus = 'AVAILABLE' WHERE t.tableNumber = :tableNumber")
    public void updateTableToAvailable (@Param ("tableNumber") String tableNumber);

    // 5. 自動更新桌位狀態與 10 分鐘保留規則整合
    // 當時間超過 cutOffTime（即超過 10 分鐘保留時間），且顧客沒有報到或取消訂位，將桌位狀態更新為 “可使用”（AVAILABLE）。
	// 當時間到達或超過 currentTime（即訂位開始時間），但還未超過 10 分鐘保留時間，桌位狀態仍保持 “已訂位”（RESERVED），直到顧客報到，此時狀態才會更新為 “用餐中”（ACTIVE）。
    @Modifying
    @Query ("UPDATE TableManagement t SET t.tableStatus = " +
            "CASE " +
            "  WHEN r.reservationStarttime <= :cutOffTime AND r.reservationStatus = 'RESERVED' THEN 'AVAILABLE' " +  // 超過 10 分鐘未報到，狀態設為 "可使用"
            "  ELSE t.tableStatus " +                                           // 保持 "已訂位" 狀態
            "END " +
            "WHERE t.tableNumber IN (SELECT r.tableManagement.tableNumber FROM ReservationManagement r " +
            "WHERE r.reservationDate = :currentDate AND r.reservationStatus = 'RESERVED')")
    public void autoUpdateTableStatusToAvailable (
        @Param ("currentDate") LocalDate currentDate,
        @Param ("cutOffTime") LocalTime cutOffTime
    );

    @Query ("SELECT r FROM Reservation r WHERE r.reservationManagement.reservationDate = :currentDate " +
    	       "AND r.reservationManagement.reservationStarttime <= :startTime")
    	List <Reservation> findReservationsStartingSoon(@Param("currentDate") LocalDate currentDate, 
    	                                               @Param("startTime") LocalTime startTime);
}