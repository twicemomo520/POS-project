package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.BusinessHours;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.BusinessHoursDao;
import com.example.pos10.repository.ReservationDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;

@Service
public class TableManagementServiceImpl implements TableManagementService {

	@Autowired
	private TableManagementDao tableManagementDao;
	
	@Autowired
	private BusinessHoursDao businessHoursDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private ReservationService reservationService;

	// 1. 創建桌位
	@Override
	@Transactional

  	public TableManagementRes createTable (TableManagementReq tableReq) {
	    // 1. 檢查桌號是否已經存在
		if (tableManagementDao.existsById(tableReq.getTableNumber())) {
            return new TableManagementRes(ResMessage.TABLE_NUMBER_ALREADY_EXIST.getCode(), ResMessage.TABLE_NUMBER_ALREADY_EXIST.getMessage());
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
	    
	    // 2. 檢查桌位是否與 Reservation 相關聯
	    if (table.getReservations() != null && !table.getReservations().isEmpty()) {
	        return new TableManagementRes(ResMessage.TABLE_HAS_RESERVATION_ID.getCode(), ResMessage.TABLE_HAS_RESERVATION_ID.getMessage());
	    }

	    // 3. 檢查桌位狀態是否允許刪除（例如桌位必須是 AVAILABLE 狀態）
	    if (!table.getTableStatus().equals(TableManagement.TableStatus.AVAILABLE)) {
	        return new TableManagementRes(ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getCode(), ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getMessage());
	    }

	    // 執行刪除操作
	    tableManagementDao.deleteByTableNumber(tableNumber);
	    return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

//	// 3. 更新座位狀態
//	@Override
//	@Transactional
//	public TableManagementRes updateTableStatus(String tableNumber, TableStatus status, LocalDate reservationDate, int diningDuration) {
//	    // 1. 檢查桌號是否存在
//	    TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
//	    if (table == null) {
//	        return new TableManagementRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
//	    }
//
//	    // 2. 獲取當天的營業時段
//	    String dayOfWeek = reservationDate.getDayOfWeek().toString();
//	    List<BusinessHours> businessHoursList = businessHoursDao.findBusinessHoursByDayAndStore(1, dayOfWeek);
//
//	    if (businessHoursList.isEmpty()) {
//	        return new TableManagementRes(ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getCode(),
//	                                       ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getMessage());
//	    }
//
//	    // 3. 確認當前時間是否在營業時間內
//	    LocalDateTime now = LocalDateTime.now();
//	    boolean isWithinBusinessHours = false;
//
//	    for (BusinessHours businessHours : businessHoursList) {
//	        LocalTime openingTime = businessHours.getOpeningTime();
//	        LocalTime closingTime = businessHours.getClosingTime();
//	        LocalDateTime openingDateTime = reservationDate.atTime(openingTime);
//	        LocalDateTime closingDateTime = reservationDate.atTime(closingTime);
//
//	        if (now.isAfter(openingDateTime) && now.isBefore(closingDateTime)) {
//	            isWithinBusinessHours = true;
//	            break;
//	        }
//	    }
//
//	    if (!isWithinBusinessHours) {
//	        return new TableManagementRes(ResMessage.BUSINESS_HOURS_TO_UPDATE_NOT_FOUND.getCode(),
//	                                       ResMessage.BUSINESS_HOURS_TO_UPDATE_NOT_FOUND.getMessage());
//	    }
//
//	    // 4. 獲取當天的所有預訂
//	    List<Reservation> reservations = reservationDao.findAllByReservationDate(reservationDate);
//
//	    // 確認所選時間段
//	    LocalTime startTime = businessHoursList.get(0).getOpeningTime(); // 營業開始時間
//	    LocalTime endTime = startTime.plusMinutes(diningDuration); // 計算結束時間
//
//	    // 檢查是否有與目標桌位相關的預訂
//	    for (Reservation reservation : reservations) {
//	        for (TableManagement reservedTable : reservation.getTables()) {
//	            if (reservedTable.getTableNumber().equals(tableNumber)) {
//	                LocalTime reservedStart = reservation.getReservationStartTime(); 
//	                LocalTime reservedEnd = reservedStart.plusMinutes(diningDuration);
//
//	                // 將 LocalTime 轉換為 LocalDateTime 進行比較
//	                LocalDateTime reservedStartDateTime = LocalDateTime.of(reservationDate, reservedStart);
//	                LocalDateTime reservedEndDateTime = LocalDateTime.of(reservationDate, reservedEnd);
//
//	                // 檢查時間段是否與預訂時間重疊
//	                if (startTime.isBefore(reservedEndDateTime.toLocalTime()) && endTime.isAfter(reservedStartDateTime.toLocalTime())) {
//	                    return new TableManagementRes(ResMessage.TABLE_RESERVATION_CONFLICT.getCode(), //
//	                    		ResMessage.TABLE_RESERVATION_CONFLICT.getMessage());
//	                }
//	            }
//	        }
//	    }
//	    
//	    // 5. 檢查狀態轉換是否被允許
//	    boolean isStatusUpdateAllowed = false;
//	    TableManagement.TableStatus currentStatus = table.getTableStatus();
//
//	    switch (currentStatus) {
//	        // AVAILABLE → ACTIVE: 顧客現場候位被安排入座
//	        case AVAILABLE:
//	            if (status == TableManagement.TableStatus.ACTIVE) {
//	                isStatusUpdateAllowed = true;
//	            }
//	            break;
//
//	        // RESERVED → ACTIVE: 預約的顧客報到
//	        case RESERVED:
//	            if (status == TableManagement.TableStatus.ACTIVE) {
//	                isStatusUpdateAllowed = true;
//	            }
//	            break;
//
//	        // ACTIVE → AVAILABLE: 顧客用餐結束
//	        case ACTIVE:
//	            if (status == TableManagement.TableStatus.AVAILABLE) {
//	                isStatusUpdateAllowed = true;
//	            }
//	            break;
//
//	        // 其他狀況不允許
//	        default:
//	            isStatusUpdateAllowed = false;
//	    }
//
//	    // 6. 如果允許狀態更新，執行資料庫更新
//	    if (isStatusUpdateAllowed) {
//	        tableManagementDao.updateTableStatus(tableNumber, status.name());
//	        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
//	    }
//
//	    // 7. 如果不允許該狀態轉換，返回錯誤訊息
//	    return new TableManagementRes(ResMessage.INVALID_STATUS_TRANSITION.getCode(), ResMessage.INVALID_STATUS_TRANSITION.getMessage());
//	}
//	
	// 4. 更新桌位編號或容納人數
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
	
	// 5. 查詢桌位狀態
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
	
	// 6. 獲取所有桌位（合併桌位顯示）
	@Override
	public List<TableManagement> getAllTables(LocalDate reservationDate, int diningDuration) {
	    List<TableManagement> allTables = tableManagementDao.findAll();

	    // 檢查是否有桌位資料
	    if (allTables.isEmpty()) {
	        throw new IllegalArgumentException(ResMessage.NO_AVAILABLE_TABLES.getMessage());
	    }

	    // 獲取當天的營業時段
	    String dayOfWeek = reservationDate.getDayOfWeek().toString();
	    List<BusinessHours> businessHoursList = businessHoursDao.findBusinessHoursByDayAndStore(1, dayOfWeek);

	    if (businessHoursList.isEmpty()) {
	        throw new IllegalArgumentException(ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getMessage());
	    }

	    // 使用 calculateAvailableStartTimes 方法計算所有可用的開始時間段
	    List<LocalTime> availableStartTimes = new ArrayList<>();
	    for (BusinessHours businessHours : businessHoursList) {
	        LocalTime openingTime = businessHours.getOpeningTime();
	        LocalTime closingTime = businessHours.getClosingTime();
	        
	        // 使用 ReservationService 的方法計算可用的開始時間段
	        availableStartTimes.addAll(reservationService.calculateAvailableStartTimes(openingTime, closingTime, diningDuration));
	    }

	    // 獲取所有的預訂
	    List<Reservation> reservations = reservationDao.findAllByReservationDate(reservationDate);

	    // 準備返回的桌位狀態列表
	    List<TableManagement> availableTables = new ArrayList<>();

	    for (TableManagement table : allTables) {
	        boolean isAvailable = true; // 假設桌位可用

	        // 過濾出與此桌位相關的預訂
	        List<Reservation> filteredReservations = new ArrayList<>();
	        for (Reservation reservation : reservations) {
	            if (reservation.getTables().contains(table)) {
	                LocalTime reservedStart = reservation.getReservationStartTime();
	                LocalTime reservedEnd = reservedStart.plusMinutes(diningDuration);

	                // 檢查桌位是否正在用餐或與選定的時間段重疊
	                if (reservedStart.isBefore(LocalTime.now().plusMinutes(diningDuration)) && reservedEnd.isAfter(LocalTime.now())) {
	                    isAvailable = false; // 當前正在用餐
	                    table.setTableStatus(TableManagement.TableStatus.ACTIVE); // 標記為正在用餐
	                } else {
	                    // 將預訂添加到過濾列表
	                    filteredReservations.add(reservation);
	                }
	            }
	        }

	        // 設置該桌位的預訂信息
	        table.setReservations(filteredReservations); 

	        // 根據可用性設置桌位狀態
	        if (isAvailable) {
	            table.setTableStatus(TableManagement.TableStatus.AVAILABLE); // 更新狀態為可用
	        } else if (table.getTableStatus() != TableManagement.TableStatus.ACTIVE) {
	            table.setTableStatus(TableManagement.TableStatus.RESERVED); // 更新狀態為已預訂
	        }

	        availableTables.add(table);
	    }

	    return availableTables; // 返回可用桌位列表
	}
}
