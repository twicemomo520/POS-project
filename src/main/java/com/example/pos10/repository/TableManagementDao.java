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
    
    // 4. 更新桌位編號和容納人數
    @Modifying
    @Query (value = "UPDATE table_management SET table_number = ?2, table_capacity = ?3 WHERE table_number = ?1", nativeQuery = true)
    public int updateTable (String oldTableNumber, String newTableNumber, int newCapacity);

    // 5. 查詢桌位狀態 - 使用 JPQL (Reservation 表用）
    // @Query("SELECT t FROM TableManagement t WHERE t.tableStatus = ?1")
    public List <TableManagement> findByTableStatus (TableManagement.TableStatus tableStatus);

    // 6. 查詢狀態為 "AVAILABLE" 的所有桌位，並根據桌位容量來排序
    @Query("SELECT t FROM TableManagement t WHERE t.tableStatus = 'AVAILABLE' ORDER BY t.tableCapacity ASC")
    public List<TableManagement> findAvailableTablesOrderedByCapacity();

    // 7. 查詢同一 reservation 的桌位（顯示合併桌位） - 使用 JPQL 查詢
    @Query ("SELECT t FROM TableManagement t JOIN t.reservations r WHERE r = :reservation")
    public List <TableManagement> findByReservations(Reservation reservation);
    
    //8.回傳所有資料
    @Query(value = " SELECT * FROM table_management  ",nativeQuery = true)
    public List<TableManagement> selectAll();
    
    
}