package com.example.POS.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.POS.project.service.ifs.TableManagementService;
import com.example.POS.project.vo.TableManagementReq;
import com.example.POS.project.vo.TableManagementRes;
import com.example.POS.project.constant.ResMessage;
import com.example.POS.project.entity.TableManagement;
import com.example.POS.project.entity.TableManagement.TableStatus;

import java.util.List;

import javax.validation.Valid;


@RestController
public class TableManagementController {

    @Autowired
    private TableManagementService tableManagementService;

    // 常見的 HTTP 請求方法：
    // 1. POST: 創建數據 -> POST 用於向服務器提交數據來創建新資料庫。HTTP狀態碼: 201 Created（資源創建成功）
    // 2. GET: 獲取數據 -> GET 用於向服務器請求數據資料庫，而不會改變伺服器上的資料庫數據，常用於查詢或檢索現有的資源。HTTP狀態碼: 200 OK（請求成功）
    // 3. PUT: 更新數據 -> PUT 用於更新現有的資料庫數據，需要傳遞新的數據，並且替換掉已有的數據。HTTP狀態碼: 200 OK 或 204 No Content（請求成功，無返回數據）
    // 4. DELETE: 刪除數據 -> DELETE 用於刪除現有的數據。HTTP狀態碼: 200 OK 或 204 No Content（刪除成功）
    
    
    // 1. 創建桌位
    @PostMapping(value = "/tableManagement/createTable")
    public TableManagementRes createTable (@RequestBody List<TableManagementReq> tableReqList) {
        for (TableManagementReq tableReq : tableReqList) {
            tableManagementService.createTable(tableReq);
        }
        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 2. 刪除桌位
    @DeleteMapping ("/tableManagement/deleteTable/{tableNumber}")
    public TableManagementRes deleteTable (@PathVariable String tableNumber) {
        return tableManagementService.deleteTable (tableNumber);
    }


    // 3. 更新桌位狀態
    @PutMapping("/tableManagement/updateTableStatus/{tableNumber}")
    public TableManagementRes updateTableStatus(@PathVariable String tableNumber, @RequestParam String status) {
        // 將 status 轉換成大寫來匹配枚舉
        String upperCaseStatus = status.toUpperCase();
        return tableManagementService.updateTableStatus(tableNumber, upperCaseStatus);
    }

    // 4. 查詢桌位狀態
    @GetMapping ("/tableManagement/searchTableStatus")
    public List <TableManagement> searchTableStatus (@RequestParam TableManagement.TableStatus status) {
        return tableManagementService.searchTableStatus (status);
    }

    // 5. 調整桌位容量
    @PutMapping ("/tableManagement/adjustTableCapacity/{tableNumber}")
    public TableManagementRes adjustTableCapacity (@PathVariable String tableNumber, @RequestParam int capacity) {
        return tableManagementService.adjustTableCapacity (tableNumber, capacity);
    }

    // 6. 分配桌位
    @GetMapping ("/tableManagement/assignTable")
    public TableManagementRes assignTable (@RequestParam int customerCount) {
        return tableManagementService.assignTable (customerCount);
    }

    // 7. 查詢所有桌位
    @GetMapping ("/tableManagement/getAllTables")
    public List <TableManagement> getAllTables () {
        return tableManagementService.getAllTables ();
    }
    

	// 8. 更新桌位（桌號或容納人數）
	@PutMapping ("/tableManagement/updateTable")
	public TableManagementRes updateTable (@RequestParam String oldTableNumber,
			@RequestParam (required = false) String newTableNumber,
			@RequestParam (required = false) Integer newCapacity) {

		// 呼叫 service 進行更新
		return tableManagementService.updateTable(oldTableNumber, newTableNumber, newCapacity != null ? newCapacity : 0);
	}
}