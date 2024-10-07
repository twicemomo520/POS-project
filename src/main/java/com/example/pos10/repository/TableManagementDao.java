package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    // 3. 更新桌位狀態
    @Modifying
    @Transactional
    @Query (value = "UPDATE table_management SET table_status = ?2 WHERE table_number = ?1", nativeQuery = true)
    public int updateTableStatus (String tableNumber, String status);

    // 4. 查詢桌位狀態 - 使用 JPQL
    // @Query("SELECT t FROM TableManagement t WHERE t.tableStatus = ?1")
    public List <TableManagement> findByTableStatus (TableManagement.TableStatus tableStatus);

    // 5. 根據狀態和桌位容量查詢 - 使用 JPQL
    // @Query("SELECT t FROM TableManagement t WHERE t.tableStatus = ?1 AND t.tableCapacity >= ?2")
    public List <TableManagement> findByTableStatusAndTableCapacityGreaterThanEqual (TableManagement.TableStatus tableStatus, int capacity);

    // 6. 調整桌位容納人數 
    @Modifying
    @Query (value = "UPDATE table_management SET table_capacity = ?2 WHERE table_number = ?1", nativeQuery = true)
    public int adjustTableCapacity (String tableNumber, int capacity);

    // 7. 查詢同一 reservation 的桌位（顯示合併桌位） - 使用 JPQL 查詢
    @Query ("SELECT t FROM TableManagement t WHERE t.reservation = ?1")
    public List <TableManagement> findByReservation(Reservation reservation);
    
    // 8. 更新桌位編號和容納人數
    @Modifying
    @Query (value = "UPDATE table_management SET table_number = ?2, table_capacity = ?3 WHERE table_number = ?1", nativeQuery = true)
    public int updateTable (String oldTableNumber, String newTableNumber, int newCapacity);
}