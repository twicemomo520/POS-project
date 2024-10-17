package com.example.pos10.service.ifs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.TableManagement;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;
import com.example.pos10.vo.TimeSlotWithTableStatusRes;

public interface TableManagementService {

    // 1. 創建桌位
    public TableManagementRes createTable (TableManagementReq tableReq);  
    
    // 2. 刪除桌位
    public TableManagementRes deleteTable (String tableNumber);

    // 3. 更新桌位編號或容納人數
    public TableManagementRes updateTable (String oldTableNumber, String newTableNumber, Integer newCapacity);

    // 4. 獲取所有桌位
    public List <TableManagement> getAllTables ();
    
    // 5. 根據時間段獲取所有桌位的狀態
    public List<TimeSlotWithTableStatusRes> getTodayTableStatuses();
    
    // 6. 根據日期查詢可使用的桌位狀態
    public List<TimeSlotWithTableStatusRes> getAvailableTableStatusesByDate(LocalDate reservationDate);
    
    // 7. 根據日期和訂位時間查詢可使用的桌位狀態
    public List<TimeSlotWithTableStatusRes> getAvailableTableStatuses(LocalDate reservationDate, LocalTime reservationStartTime, LocalTime reservationEndingTime);
}