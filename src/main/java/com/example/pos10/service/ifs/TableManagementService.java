package com.example.pos10.service.ifs;

import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.entity.TableManagement.TableStatus;

import java.util.List;

public interface TableManagementService {

    // 1. 創建桌位
    public TableManagementRes createTable (TableManagementReq tableReq);  
    
    // 2. 刪除桌位
    public TableManagementRes deleteTable (String tableNumber);

    // 3. 更新桌位狀態，將 String status 改為 TableStatus
    public TableManagementRes updateTableStatus (String tableNumber, String upperCaseStatus);
    
    // 4. 更新桌位編號或容納人數
    public TableManagementRes updateTable (String oldTableNumber, String newTableNumber, Integer newCapacity);

    // 5. 查詢桌位狀態
    public List <TableManagement> searchTableStatus (TableStatus status); 

    // 6. 獲取所有桌位
    public List <TableManagement> getAllTables ();
}