package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;
//import com.example.pos10.repository.BusinessHoursDao;
import com.example.pos10.repository.ReservationDao;
import com.example.pos10.repository.TableManagementDao;
//import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;

@Service
public class TableManagementServiceImpl implements TableManagementService {

	@Autowired
	private TableManagementDao tableManagementDao;
	
//	@Autowired
//	private BusinessHoursDao businessHoursDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
//	@Autowired
//	private ReservationService reservationService;

	// 1. 創建桌位
	@Override
	@Transactional
	public TableManagementRes createTable(TableManagementReq tableReq) {
	    // 1. 檢查桌號是否已經存在
	    if (tableManagementDao.existsById(tableReq.getTableNumber())) {
	        return new TableManagementRes(ResMessage.TABLE_NUMBER_ALREADY_EXIST.getCode(), 
	                                      ResMessage.TABLE_NUMBER_ALREADY_EXIST.getMessage());
	    }

	    // 2. 檢查桌位容納人數是否合理
	    if (tableReq.getTableCapacity() <= 0) {
	        return new TableManagementRes(ResMessage.INVALID_TABLE_CAPACITY.getCode(), 
	                                      ResMessage.INVALID_TABLE_CAPACITY.getMessage());
	    }

	    // 3. 檢查桌位狀態是否為空
	    if (tableReq.getTableStatus() == null) {
	        return new TableManagementRes(ResMessage.NULL_OR_EMPTY_TABLE_STATUS.getCode(), 
	                                      ResMessage.NULL_OR_EMPTY_TABLE_STATUS.getMessage());
	    }

	    // 4. 檢查桌號格式是否正確（根據你的需求自定義格式）
	    if (!tableReq.getTableNumber().matches("[A-Z]\\d{2}")) {
	        return new TableManagementRes(ResMessage.INVALID_TABLE_NUMBER_NAME.getCode(), 
	                                      ResMessage.INVALID_TABLE_NUMBER_NAME.getMessage());
	    }

	    // 不需要轉換 tableStatus 為大寫，因為現在是中文枚舉
	    System.out.println("Inserting table status: " + tableReq.getTableStatus());

	    // 插入新桌位
	    tableManagementDao.insertTableNumber(
	        tableReq.getTableNumber(), 
	        tableReq.getTableCapacity(), 
	        tableReq.getTableStatus().name()  // 直接存儲中文枚舉值
	    );

	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	// 2. 刪除桌位
	@Override
	@Transactional
	public TableManagementRes deleteTable(String tableNumber) {
	    // 1. 檢查桌號是否存在
	    TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
	    if (table == null) {
	        return new TableManagementRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), 
	                                      ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
	    }

	    // 2. 檢查桌位是否與 Reservation 相關聯
	    if (table.getReservations() != null && !table.getReservations().isEmpty()) {
	        return new TableManagementRes(ResMessage.TABLE_HAS_RESERVATION_ID.getCode(), 
	                                      ResMessage.TABLE_HAS_RESERVATION_ID.getMessage());
	    }

	    // 3. 檢查桌位狀態是否允許刪除（例如桌位必須是 可使用 狀態）
	    if (!table.getTableStatus().equals(TableManagement.TableStatus.可使用)) {
	        return new TableManagementRes(ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getCode(), 
	                                      ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getMessage());
	    }

	    // 執行刪除操作
	    tableManagementDao.deleteByTableNumber(tableNumber);
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 3. 更新桌位編號或容納人數
	@Override
	public TableManagementRes updateTable(String oldTableNumber, String newTableNumber, Integer newCapacity) {
	    // 1. 檢查舊的桌號是否存在
	    TableManagement table = tableManagementDao.findById(oldTableNumber).orElse(null);
	    if (table == null) {
	        return new TableManagementRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), 
	                                      ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
	    }

	    // 2. 更新容納人數（如果有提供新的容納人數）
	    if (newCapacity != null && newCapacity > 0) {
	        table.setTableCapacity(newCapacity);
	    }

	    // 3. 如果有新的桌號，檢查新桌號是否已存在
	    if (newTableNumber != null && !newTableNumber.isEmpty() && !newTableNumber.equals(oldTableNumber)) {
	        if (tableManagementDao.existsById(newTableNumber)) {
	            return new TableManagementRes(ResMessage.TABLE_NUMBER_ALREADY_EXIST.getCode(), 
	                                          ResMessage.TABLE_NUMBER_ALREADY_EXIST.getMessage());
	        }

	        // 更新桌號
	        table.setTableNumber(newTableNumber);
	        tableManagementDao.save(table);

	        // 刪除舊的桌號
	        tableManagementDao.deleteById(oldTableNumber);

	        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	    }

	    // 4. 如果只有更新容納人數，保存更新的桌位
	    tableManagementDao.save(table);
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 4. 獲取所有桌位
	@Override
	public List<TableManagement> getAllTables() {
	    List<TableManagement> allTables = tableManagementDao.findAll();

	    // 檢查是否有桌位資料
	    if (allTables.isEmpty()) {
	        throw new IllegalArgumentException(ResMessage.NO_AVAILABLE_TABLES.getMessage());
	    }

	    return allTables;
	}
	
//	// 5. 根據時間段獲取所有桌位的狀態
//
//	@Override
//	public List<TableManagement> getTableStatusesByTimeSlot(LocalDate reservationDate, LocalTime startTime,
//			LocalTime endTime) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	// 6. 檢查特定時間段內桌位的可用性
//
//	@Override
//	public boolean isTableAvailable(String tableNumber, LocalDate reservationDate, LocalTime startTime,
//			LocalTime endTime) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	
//	// 7. 自動更新桌位狀態
//
//	@Override
//	public void autoUpdateTableStatuses() {
//		// TODO Auto-generated method stub
//		
//	}
	
	
}
