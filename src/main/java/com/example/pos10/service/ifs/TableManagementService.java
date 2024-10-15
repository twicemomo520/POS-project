package com.example.pos10.service.ifs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.TableManagement;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;

public interface TableManagementService {

    // 1. 創建桌位
    public TableManagementRes createTable (TableManagementReq tableReq);  
    
    // 2. 刪除桌位
    public TableManagementRes deleteTable (String tableNumber);

    // 3. 更新桌位編號或容納人數
    public TableManagementRes updateTable (String oldTableNumber, String newTableNumber, Integer newCapacity);

    // 4. 獲取所有桌位
    public List <TableManagement> getAllTables ();
    
//    // 5. 根據時間段獲取所有桌位的狀態
//    public List<TableManagement> getTableStatusesByTimeSlot(LocalDate reservationDate, LocalTime startTime, LocalTime endTime);
//    
//    // 6. 檢查特定時間段內桌位的可用性
//    public boolean isTableAvailable(String tableNumber, LocalDate reservationDate, LocalTime startTime, LocalTime endTime);
//    
//    // 7. 自動更新桌位狀態
//    public void autoUpdateTableStatuses();
}