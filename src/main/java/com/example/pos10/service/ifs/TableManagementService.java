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

    // 4. 查詢桌位狀態
    public List <TableManagement> searchTableStatus (TableStatus status); 

    // 5. 調整桌位容納人數
    public TableManagementRes adjustTableCapacity (String tableNumber, int capacity);

    // 6. 分配桌位
    public TableManagementRes assignTable (int customerCount);

    // 7. 查詢所有桌位
    public List <TableManagement> getAllTables ();
    
    // 8. 更新桌位編號或容納人數
    public TableManagementRes updateTable (String oldTableNumber, String newTableNumber, Integer newCapacity);
}