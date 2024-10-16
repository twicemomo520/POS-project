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
import com.example.pos10.entity.TableManagement.TableStatus;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Integer> {

	// 1. 儲存訂位
	@Override
	<S extends Reservation> S save(S reservation);

    // 2. 根據顧客電話號碼查詢訂位
    @Query("SELECT r FROM Reservation r WHERE r.customerPhoneNumber = :phoneNumber")
    List<Reservation> findByCustomerPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    @Query("SELECT r FROM Reservation r WHERE r.customerPhoneNumber LIKE %:lastThreeDigits")
    List<Reservation> findByCustomerPhoneNumberEndingWith(@Param("lastThreeDigits") String lastThreeDigits);
    
    // 3. 查詢某日期的所有訂位
    @Query("SELECT r FROM Reservation r JOIN FETCH r.tables WHERE r.reservationDate = :reservationDate")
    List<Reservation> findAllByReservationDate(@Param("reservationDate") LocalDate reservationDate);
    
    // 4. 手動取消訂位
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.id IN " +
           "(SELECT r2.id FROM Reservation r2 JOIN r2.tables t WHERE t.tableNumber = :tableNumber)")
    public void deleteByTableNumber(@Param("tableNumber") String tableNumber);
    
    // 5. 自動更新逾時取消桌位狀態
    // 5-1 查詢符合條件自動更新過期桌位的列表。
    @Query("SELECT t.tableNumber FROM TableManagement t " +
    	       "JOIN t.reservations r " +
    	       "WHERE r.reservationDate = :currentDate " +
    	       "AND r.reservationStartTime < :cutOffTime " +
    	       "AND t.tableStatus = '訂位中'")  // 修改為新的枚舉值
    List<String> findReservedTables(@Param("currentDate") LocalDate currentDate, @Param("cutOffTime") LocalTime cutOffTime);
    
    // 6-2 查詢出的桌號列表將對應的桌位狀態更新為 “AVAILABLE”
    @Modifying
    @Query("UPDATE TableManagement t SET t.tableStatus = '可使用' " +  // 修改為新的枚舉值
           "WHERE t.tableNumber IN :tableNumbers")
    public int updateTableStatusToAvailable(@Param("tableNumbers") List<String> tableNumbers);
    
    // 6-3 查找與桌位相關聯的過期訂位
    @Query("SELECT r.id FROM Reservation r JOIN r.tables t WHERE t.tableNumber = :tableNumber")
    List<Integer> findReservationIdsByTableNumber(@Param("tableNumber") String tableNumber);
    
    // 6-4 查詢出的訂位 ID 列表批量刪除對應的訂位記錄
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.id IN :reservationIds")
    public void deleteByReservationIds(@Param("reservationIds") List<Integer> reservationIds);

    // 7. 手動報到更新桌位狀態
    @Modifying
    @Query("UPDATE TableManagement t " +
           "SET t.tableStatus = :newStatus " +  
           "WHERE t.tableNumber = :tableNumber " + 
           "AND t.tableStatus = :currentStatus")
    public int manualCheckIn(@Param("tableNumber") String tableNumber,
                             @Param("newStatus") TableStatus newStatus,
                             @Param("currentStatus") TableStatus currentStatus);
}