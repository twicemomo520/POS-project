package com.example.pos10.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;

@Service
public class TableManagementServiceImpl implements TableManagementService {

	@Autowired
	private TableManagementDao tableManagementDao;

	// 1. 創建桌位
	@Override
	@Transactional
	public TableManagementRes createTable (TableManagementReq tableReq) {
	    // 1. 檢查桌號是否已經存在
	    if (tableManagementDao.existsById(tableReq.getTableNumber())) {
	        return new TableManagementRes (ResMessage.TABLE_NUMBER_ALREADY_EXIST.getCode(), ResMessage.TABLE_NUMBER_ALREADY_EXIST.getMessage());
	    }
	    
	    // 2. 檢查桌位容納人數是否合理
	    if (tableReq.getTableCapacity() <= 0) {
	        return new TableManagementRes (ResMessage.INVALID_TABLE_CAPACITY.getCode(), ResMessage.INVALID_TABLE_CAPACITY.getMessage());
	    }

	    // 3. 檢查桌位狀態是否為空
	    if (tableReq.getTableStatus() == null) {
	        return new TableManagementRes(ResMessage.NULL_OR_EMPTY_TABLE_STATUS.getCode(), ResMessage.NULL_OR_EMPTY_TABLE_STATUS.getMessage());
	    }

	    // 4. 檢查桌號格式是否正確（根據你的需求自定義格式）
	    if (!tableReq.getTableNumber().matches("[A-Z]\\d{2}")) {
	        return new TableManagementRes(ResMessage.INVALID_TABLE_NUMBER_NAME.getCode(), ResMessage.INVALID_TABLE_NUMBER_NAME.getMessage());
	    }
	    
	    // 將 tableStatus 強制轉換為大寫，確保儲存時一致
	    String statusUpperCase = tableReq.getTableStatus().name().toUpperCase();
	    
	    System.out.println("Inserting table status: " + statusUpperCase);

	    // 插入新桌位
	    tableManagementDao.insertTableNumber(tableReq.getTableNumber(), tableReq.getTableCapacity(), statusUpperCase);
	    
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	// 2. 刪除桌位
	@Override
	@Transactional
	public TableManagementRes deleteTable (String tableNumber) {
	    // 1. 檢查桌號是否存在
	    TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
	    if (table == null) {
	        return new TableManagementRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
	    }
	    
//	    // 2. 檢查桌位是否與 Reservation 相關聯
//	    if (table.getReservation() != null) {
//	        return new TableManagementRes(ResMessage.TABLE_HAS_RESERVATION_ID.getCode(), ResMessage.TABLE_HAS_RESERVATION_ID.getMessage());
//	    }

	    // 3. 檢查桌位狀態是否允許刪除（例如桌位必須是 AVAILABLE 狀態）
	    if (!table.getTableStatus().equals(TableManagement.TableStatus.AVAILABLE)) {
	        return new TableManagementRes(ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getCode(), ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getMessage());
	    }

	    // 執行刪除操作
	    tableManagementDao.deleteByTableNumber(tableNumber);
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 3. 更新座位
	@Override
	@Transactional
	public TableManagementRes updateTableStatus (String tableNumber, String upperCaseStatus) {
	    // 1. 檢查桌號是否存在
	    TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
	    if (table == null) {
	        return new TableManagementRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
	    }

	    // 2. 將狀態字串轉換為枚舉值
	    TableManagement.TableStatus newStatus;
	    try {
	        newStatus = TableManagement.TableStatus.valueOf(upperCaseStatus);
	    } catch (IllegalArgumentException e) {
	        return new TableManagementRes(ResMessage.INVALID_TABLE_STATUS.getCode(), ResMessage.INVALID_TABLE_STATUS.getMessage());
	    }

	    // 3. 取得當前桌位的狀態
	    TableManagement.TableStatus currentStatus = table.getTableStatus();

	    // 4. 檢查狀態轉換是否被允許
	    boolean isStatusUpdateAllowed = false;

	    switch (currentStatus) {
	        // AVAILABLE → ACTIVE: 顧客現場候位被安排入座
	        case AVAILABLE:
	            if (newStatus == TableManagement.TableStatus.ACTIVE) {
	                isStatusUpdateAllowed = true;
	            }
	            break;

	        // RESERVED → ACTIVE: 預約的顧客報到
	        case RESERVED:
	            if (newStatus == TableManagement.TableStatus.ACTIVE) {
	                isStatusUpdateAllowed = true;
	            }
	            break;

	        // ACTIVE → AVAILABLE: 顧客用餐結束
	        case ACTIVE:
	            if (newStatus == TableManagement.TableStatus.AVAILABLE) {
	                isStatusUpdateAllowed = true;
	            }
	            break;

	        // 其他狀況不允許
	        default:
	            isStatusUpdateAllowed = false;
	    }

	    // 5. 如果允許狀態更新，執行資料庫更新
	    if (isStatusUpdateAllowed) {
	        tableManagementDao.updateTableStatus(tableNumber, newStatus.name());
	        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	    }

	    // 6. 如果不允許該狀態轉換，返回錯誤訊息
	    return new TableManagementRes(ResMessage.INVALID_STATUS_TRANSITION.getCode(), ResMessage.INVALID_STATUS_TRANSITION.getMessage());
	}
	
	// 4. 查詢桌位狀態
	@Override
	public List <TableManagement> searchTableStatus (TableManagement.TableStatus status) {
	    // 1. 檢查狀態是否為 null（這步驟可以根據實際需求選擇是否保留）
	    if (status == null) {
	        throw new IllegalArgumentException(ResMessage.NULL_OR_EMPTY_TABLE_STATUS.getMessage());
	    }

	    // 2. 查詢桌位狀態
	    List <TableManagement> tables = tableManagementDao.findByTableStatus(status);
	    
	    // 3. 檢查查詢結果是否為空
	    if (tables.isEmpty()) {
	        return tables;
	    }
	    return tables;
	}
	
	// 5. 調整桌位容納人數
	@Override
	@Transactional
	public TableManagementRes adjustTableCapacity (String tableNumber, int capacity) {
	    // 1. 檢查桌號是否存在
	    TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
	    if (table == null) {
	        return new TableManagementRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
	    }

	    // 2. 檢查容納人數是否有效（必須大於 0）
	    if (capacity <= 0) {
	        return new TableManagementRes(ResMessage.INVALID_TABLE_CAPACITY.getCode(), ResMessage.INVALID_TABLE_CAPACITY.getMessage());
	    }

	    // 3. 檢查桌位當前狀態（如果不允許在 RESERVED 或 ACTIVE 狀態下調整容量，則返回錯誤）
	    if (table.getTableStatus() == TableManagement.TableStatus.RESERVED || 
	        table.getTableStatus() == TableManagement.TableStatus.ACTIVE) {
	        return new TableManagementRes(ResMessage.TABLE_STATUS_NOT_ALLOWED_FOR_CAPACITY_CHANGE.getCode(), 
	                                      ResMessage.TABLE_STATUS_NOT_ALLOWED_FOR_CAPACITY_CHANGE.getMessage());
	    }

	    // 4. 執行調整容納人數的操作
	    tableManagementDao.adjustTableCapacity(tableNumber, capacity);
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 6. 分配桌位
	@Override
	public TableManagementRes assignTable (int customerCount) {
	    // 1. 檢查顧客人數是否有效
	    if (customerCount <= 0) {
	        return new TableManagementRes(ResMessage.INVALID_CUSTOMER_COUNT.getCode(), ResMessage.INVALID_CUSTOMER_COUNT.getMessage());
	    }

	    // 2. 查找符合條件的桌位（狀態為 AVAILABLE 且 容納人數 >= 顧客人數）
	    List <TableManagement> availableTables = tableManagementDao.findByTableStatusAndTableCapacityGreaterThanEqual(TableManagement.TableStatus.AVAILABLE, customerCount);
	    
	    // 3. 檢查是否有符合需求的桌位
	    if (availableTables.isEmpty()) {
	        return new TableManagementRes(ResMessage.NO_AVAILABLE_TABLES.getCode(), ResMessage.NO_AVAILABLE_TABLES.getMessage());
	    }

	    // 4. 返回符合條件的桌位清單和成功訊息
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), availableTables);
	}

	// 7. 查詢所有桌位（合併桌位顯示）
	@Override
	public List <TableManagement> getAllTables() {
	    List <TableManagement> allTables = tableManagementDao.findAll();
	    
	    // 檢查是否有桌位資料
	    if (allTables.isEmpty()) {
	        throw new IllegalArgumentException(ResMessage.NO_AVAILABLE_TABLES.getMessage());
	    }

	    return allTables;
	}

	// 8. 更新桌位編號或容納人數
	@Override
	public TableManagementRes updateTable(String oldTableNumber, String newTableNumber, Integer newCapacity) {
	    // 1. 檢查舊的桌號是否存在
	    TableManagement table = tableManagementDao.findById(oldTableNumber).orElse(null);
	    if (table == null) {
	        return new TableManagementRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
	    }

	    // 2. 更新容納人數（如果有提供新的容納人數）
	    if (newCapacity != null && newCapacity > 0) {
	        table.setTableCapacity(newCapacity);
	    }

	    // 3. 如果有新的桌號，檢查新桌號是否已存在
	    if (newTableNumber != null && !newTableNumber.isEmpty() && !newTableNumber.equals(oldTableNumber)) {
	        if (tableManagementDao.existsById(newTableNumber)) {
	            return new TableManagementRes(ResMessage.TABLE_NUMBER_ALREADY_EXIST.getCode(), ResMessage.TABLE_NUMBER_ALREADY_EXIST.getMessage());
	        }
	        
	        // 刪除舊的桌位
	        tableManagementDao.deleteById(oldTableNumber);

	        // 建立新的桌位
	        table.setTableNumber(newTableNumber);
	        tableManagementDao.save(table);
	        
	        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	    }

	    // 4. 保存資料庫中更新的其他欄位
	    tableManagementDao.save(table);
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
}
