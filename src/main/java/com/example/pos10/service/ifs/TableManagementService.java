package com.example.pos10.service.ifs;

import java.time.LocalDate;
import java.util.List;

import com.example.pos10.entity.TableManagement;
import com.example.pos10.entity.TableManagement.TableStatus;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;

public interface TableManagementService {

    // 1. 創建桌位
    public TableManagementRes createTable (TableManagementReq tableReq);  
    
    // 2. 刪除桌位
    public TableManagementRes deleteTable (String tableNumber);

//    // 3. 時間段更新桌位狀態
//    public TableManagementRes updateTableStatus(String tableNumber, TableStatus status, LocalDate reservationDate, int diningDuration);
//    
    // 4. 更新桌位編號或容納人數
    public TableManagementRes updateTable (String oldTableNumber, String newTableNumber, Integer newCapacity);

    // 5. 查詢桌位狀態
    public List <TableManagement> searchTableStatus (TableStatus status); 

    // 6. 獲取所有桌位
    public List <TableManagement> getAllTables (LocalDate reservationDate, int diningDuration);
}