package com.example.pos10.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;
import com.example.pos10.vo.TimeSlotWithTableStatusRes;


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

    // 3. 更新桌位（桌號或容納人數）
 	@PutMapping ("/tableManagement/updateTable")
 	public TableManagementRes updateTable (@RequestParam String oldTableNumber,
 			@RequestParam (required = false) String newTableNumber,
 			@RequestParam (required = false) Integer newCapacity) {

 		// 呼叫 service 進行更新
 		return tableManagementService.updateTable(oldTableNumber, newTableNumber, newCapacity != null ? newCapacity : 0);
 	}

 	// 4. 查詢所有桌位
    @GetMapping ("/tableManagement/getAllTables")
    public List <TableManagement> getAllTables () {
        return tableManagementService.getAllTables ();
    }

    // 5. 根據時間段獲取所有桌位的狀態
    @GetMapping("/tableManagement/getTodayTableStatuses")
    public List<TimeSlotWithTableStatusRes> getTodayTableStatuses() {
        return tableManagementService.getTodayTableStatuses();
    }
    
    // 6. 根據日期查詢可使用的桌位狀態
    @GetMapping("/tableManagement/getAvailableTableStatusesByDate")
    public List<TimeSlotWithTableStatusRes> getAvailableTableStatusesByDate (
    		 @RequestParam ("reservationDate") @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate) {
        return tableManagementService.getAvailableTableStatusesByDate(reservationDate);
    }
    
    // 7. 根據日期和訂位時間查詢可使用的桌位狀態
    @GetMapping("/tableManagement/getAvailableTableStatuses")
    public List<TimeSlotWithTableStatusRes> getAvailableTableStatuses (
             @RequestParam("reservationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
             @RequestParam("reservationStartTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime reservationStartTime,
             @RequestParam("reservationEndTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime reservationEndingTime) {
        return tableManagementService.getAvailableTableStatuses(reservationDate, reservationStartTime, reservationEndingTime);
    }
}