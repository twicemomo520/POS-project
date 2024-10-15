package com.example.pos10.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;

@Repository
public interface TableManagementDao extends JpaRepository <TableManagement, String> {

    // 1. 創建桌位
    @Modifying
    @Transactional
    @Query (value = "INSERT INTO table_management (table_number, table_capacity, table_status) VALUES (?1, ?2, ?3)", nativeQuery = true)
    public int insertTableNumber (String tableNumber, int tableCapacity, String tableStatus);

    // 2. 刪除桌位
    @Modifying
    @Transactional
    @Query (value = "DELETE FROM table_management WHERE table_number = ?1", nativeQuery = true)
    public int deleteByTableNumber (String tableNumber);
    
    // 3. 更新桌位編號和容納人數
    @Modifying
    @Query (value = "UPDATE table_management SET table_number = ?2, table_capacity = ?3 WHERE table_number = ?1", nativeQuery = true)
    public int updateTable (String oldTableNumber, String newTableNumber, int newCapacity);
    
//    // 4. 根據預訂日期和時間段查詢所有桌位狀態
//    @Query("SELECT t FROM TableManagement t LEFT JOIN Reservation r ON t.tableNumber = r.tableNumber "
//         + "WHERE (r.reservationDate IS NULL OR (r.startTime >= :endTime OR r.endTime <= :startTime))")
//    public List<TableManagement> findTableStatusesByTimeSlot (@Param("reservationDate") LocalDate reservationDate,
//                                                              @Param("startTime") LocalTime startTime,
//                                                              @Param("endTime") LocalTime endTime);
//
//    // 5. 檢查特定桌位在指定時間段內是否被預訂
//    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.tableNumber = :tableNumber " +
//    	       "AND r.reservationDate = :reservationDate " +
//    	       "AND ((r.startTime < :endTime AND r.endTime > :startTime))")
//    public boolean existsByTableNumberAndReservationTime(@Param("tableNumber") String tableNumber,
//    	                                                     @Param("reservationDate") LocalDate reservationDate,
//    	                                                     @Param("startTime") LocalTime startTime,
//    	                                                     @Param("endTime") LocalTime endTime);
//
//    // 6. 查詢狀態為 "AVAILABLE" 的所有桌位，並根據桌位容量來排序
//    @Query("SELECT t FROM TableManagement t WHERE t.tableStatus = 'AVAILABLE' ORDER BY t.tableCapacity ASC")
//    public List<TableManagement> findAvailableTablesOrderedByCapacity();
//
//    // 7. 查詢同一 reservation 的桌位（顯示合併桌位） - 使用 JPQL 查詢
//    @Query ("SELECT t FROM TableManagement t JOIN t.reservations r WHERE r = :reservation")
//    public List <TableManagement> findByReservations(Reservation reservation);
}