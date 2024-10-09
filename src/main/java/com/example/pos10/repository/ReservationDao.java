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

@Repository
public interface ReservationDao extends JpaRepository <Reservation, Integer> {

	// 1. 儲存訂位
    @Override
    <S extends Reservation> S save (S reservation);

    // 2. 根據顧客電話號碼查詢訂位
    @Query ("SELECT r FROM Reservation r WHERE r.customerPhoneNumber = :phoneNumber")
    List <Reservation> findByCustomerPhoneNumber (@Param ("phoneNumber") String phoneNumber);

    // 3. 查詢某日期的所有訂位（不透過 ReservationManagement）
    @Query ("SELECT r FROM Reservation r WHERE r.reservationManagement.reservationDate = :reservationDate")
    List <Reservation> findAllByReservationDate (@Param ("reservationDate") LocalDate reservationDate);

    // 4. 取消訂位（同時將桌位狀態更新為 'AVAILABLE'）
    @Modifying
    @Query ("UPDATE TableManagement t SET t.tableStatus = 'AVAILABLE' WHERE t.tableNumber = :tableNumber")
    public void updateTableToAvailable (@Param ("tableNumber") String tableNumber);

    // 5. 自動更新桌位狀態與 10 分鐘保留規則整合
    @Modifying
    @Query("UPDATE TableManagement t " +
           "SET t.tableStatus = CASE " +
           "   WHEN (SELECT r.reservationStarttime FROM ReservationManagement r WHERE r.tableManagement = t AND r.reservationDate = :currentDate) <= :cutOffTime " +
           "       AND t.tableStatus = 'RESERVED' THEN 'AVAILABLE' " +
           "   ELSE t.tableStatus " +
           "END " +
           "WHERE t.tableNumber IN (SELECT r.tableManagement.tableNumber FROM ReservationManagement r WHERE r.reservationDate = :currentDate)")
    public int autoUpdateTableStatusToAvailable(@Param("currentDate") LocalDate currentDate, @Param("cutOffTime") LocalTime cutOffTime);
}