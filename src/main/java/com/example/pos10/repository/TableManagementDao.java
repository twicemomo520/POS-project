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
	
	// 4. 查詢某個時間段內所有可用的桌位
    @Query("SELECT t FROM TableManagement t WHERE t.tableStatus = '可使用' "
    	    + "AND NOT EXISTS (SELECT r FROM Reservation r WHERE t MEMBER OF r.tables "
    	    + "AND r.reservationDate = :reservationDate "
    	    + "AND ((:startTime < r.reservationEndingTime AND :startTime >= r.reservationStartTime) "
    	    + "OR (:endTime > r.reservationStartTime AND :endTime <= r.reservationEndingTime)))")
    List<TableManagement> findAvailableTablesInTimeSlot(
    	    @Param("reservationDate") LocalDate reservationDate,
    	    @Param("startTime") LocalTime startTime,
    	    @Param("endTime") LocalTime endTime);

	// 5. 查詢狀態為 "AVAILABLE" 的所有桌位，並根據桌位容量來排序
	@Query("SELECT t FROM TableManagement t WHERE t.tableStatus = '可使用' ORDER BY t.tableCapacity ASC")
	public List<TableManagement> findAvailableTablesOrderedByCapacity();

	// 6. 查詢同一 reservation 的桌位（顯示合併桌位） - 使用 JPQL 查詢
	@Query("SELECT t FROM TableManagement t JOIN t.reservations r WHERE r = :reservation")
	public List<TableManagement> findByReservations(Reservation reservation);
	
	// 7. 更改桌位的狀態
	@Modifying
	@Transactional
    @Query("UPDATE TableManagement t SET t.tableStatus = :status WHERE t.tableNumber = :tableNumber")
    public int updateTableStatus(@Param("tableNumber") String tableNumber, @Param("status") TableManagement.TableStatus status);
	
    //8.回傳所有資料
    @Query(value = " SELECT * FROM table_management WHERE table_status = '用餐中'  ",nativeQuery = true)
    public List<TableManagement> selectAll();

    
//    // 5. 查詢特定桌位在特定日期是否有預訂（先保留可能之後會在自動更新桌位狀態用到？）
//    @Query("SELECT COUNT(r) > 0 FROM Reservation r JOIN r.tables t WHERE t.tableNumber = :tableNumber AND r.reservationDate = :reservationDate")
//    public boolean existsByTableNumberAndDate(@Param("tableNumber") String tableNumber, @Param("reservationDate") LocalDate reservationDate);

}