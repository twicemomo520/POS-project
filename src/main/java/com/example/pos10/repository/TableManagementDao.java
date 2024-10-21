package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

	// 4. 更改桌位的狀態
	@Modifying
	@Transactional
    @Query("UPDATE TableManagement t SET t.tableStatus = :status WHERE t.tableNumber = :tableNumber")
    public int updateTableStatus (@Param("tableNumber") String tableNumber, @Param("status") TableManagement.TableStatus status);
	
	// 5. 桌位號碼列表查找對應的桌位
    List <TableManagement> findByTableNumberIn (List <String> tableNumbers);
    
    @Query("SELECT t FROM TableManagement t WHERE t.tableStatus = :tableStatus")
    List <TableManagement> findByTableStatus(@Param("tableStatus") TableManagement.TableStatus tableStatus);
    
    // 6. 回傳所有資料
    @Query (value = " SELECT * FROM table_management WHERE table_status = '用餐中'  ",nativeQuery = true)
    public List <TableManagement> selectAll();
}