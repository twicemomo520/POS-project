package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.entity.OperatingHours;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.ReservationAndTable;
import com.example.pos10.entity.ReservationAndTable.TableStatus;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.OperatingHoursDao;
import com.example.pos10.repository.ReservationAndTableDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.OperatingHoursService;
import com.example.pos10.service.ifs.ReservationAndTableService;
import com.example.pos10.vo.ReservationAndTableReq;
import com.example.pos10.vo.ReservationAndTableRes;

@Service
public class ReservationAndTableServiceImpl implements ReservationAndTableService{

	@Autowired
    private ReservationAndTableDao reservationAndTableDao;
	
	@Autowired
	private TableManagementDao tableManagementDao;
	
	@Autowired
	private OperatingHoursService operatingHoursService;
	
	@Autowired
	private OperatingHoursDao operatingHoursDao;
	
	// 轉換 DayOfWeek 到 OperatingHours.DayOfWeek
	private OperatingHours.DayOfWeek convertToOperatingHoursDayOfWeek(java.time.DayOfWeek javaDayOfWeek) {
	    switch (javaDayOfWeek) {
	        case MONDAY:
	            return OperatingHours.DayOfWeek.星期一;
	        case TUESDAY:
	            return OperatingHours.DayOfWeek.星期二;
	        case WEDNESDAY:
	            return OperatingHours.DayOfWeek.星期三;
	        case THURSDAY:
	            return OperatingHours.DayOfWeek.星期四;
	        case FRIDAY:
	            return OperatingHours.DayOfWeek.星期五;
	        case SATURDAY:
	            return OperatingHours.DayOfWeek.星期六;
	        case SUNDAY:
	            return OperatingHours.DayOfWeek.星期日;
	        default:
	            throw new IllegalArgumentException("無法獲取星期幾");
	    }
	}
	
	// 驗證輸入的參數是否有效
	private void validateReservationInput(LocalDate reservationDate, LocalTime reservationStartTime, LocalTime reservationEndTime) {
	    // 檢查預約日期是否為空
	    if (reservationDate == null) {
	        throw new IllegalArgumentException("預約日期不能為空");
	    }

	    // 檢查預約日期是否為過去的日期
	    if (reservationDate.isBefore(LocalDate.now())) {
	        throw new IllegalArgumentException("預約日期不能是過去的日期");
	    }

	    // 檢查預約開始和結束時間是否為空
	    if (reservationStartTime == null || reservationEndTime == null) {
	        throw new IllegalArgumentException("預約時間不能為空");
	    }

	    // 檢查開始時間是否早於結束時間
	    if (!reservationStartTime.isBefore(reservationEndTime)) {
	        throw new IllegalArgumentException("預約結束時間必須晚於開始時間");
	    }
	}

    // 1. 查詢與預約或桌位狀態關聯的所有桌位列表
	@Override
	public List<TableManagement> getTablesForReservation(int reservationId, List<TableStatus> statuses) {
	    // 1. 驗證 reservationId 是否為合法值
	    if (reservationId <= 0) {
	        throw new IllegalArgumentException("無效的 reservationId，必須是正數");
	    }

	    // 2. 檢查 reservationId 是否存在於資料庫中
	    boolean exists = reservationAndTableDao.existsByReservation_ReservationId(reservationId);
	    if (!exists) {
	        throw new IllegalArgumentException("預約 ID " + reservationId + " 不存在");
	    }

	    // 3. 如果 statuses 為 null 或空，則查詢所有狀態的桌位
	    if (statuses == null || statuses.isEmpty()) {
	        statuses = Arrays.asList(TableStatus.values()); // 使用所有的狀態
	    }

	    // 4. 查詢指定狀態下的桌位
	    List<TableManagement> tables = reservationAndTableDao.findTablesByReservationIdAndStatus(reservationId, statuses);

	    // 5. 檢查是否有查詢結果
	    if (tables.isEmpty()) {
	        throw new IllegalStateException("預約 ID " + reservationId + " 沒有關聯的桌位");
	    }

	    return tables;
	}

    // 2. 查詢與桌位相關的所有預約
	@Override
	public List<Reservation> getReservationsForTable(String tableNumber, List<TableStatus> statuses) {
	    // 1. 驗證 tableNumber 是否為 null 或空
	    if (tableNumber == null || tableNumber.trim().isEmpty()) {
	        throw new IllegalArgumentException("無效的桌位號碼，不能為空");
	    }

	    // 2. 檢查 tableNumber 是否存在於資料庫中
	    boolean tableExists = reservationAndTableDao.existsByTableNumber(tableNumber);
	    if (!tableExists) {
	        throw new IllegalArgumentException("桌位號碼 " + tableNumber + " 不存在");
	    }

	    // 3. 如果 statuses 為 null 或空，則查詢所有狀態的預約
	    if (statuses == null || statuses.isEmpty()) {
	        statuses = Arrays.asList(TableStatus.values()); // 使用所有的狀態
	    }

	    // 4. 查詢並檢查結果
	    List<Reservation> reservations = reservationAndTableDao.findReservationsByTableNumberAndStatus(tableNumber, statuses);
	    if (reservations.isEmpty()) {
	        throw new IllegalStateException("桌位號碼 " + tableNumber + " 沒有關聯的預約");
	    }

	    return reservations;
	}
    
    // 3. 檢查桌位是否在指定時間段內被預約（避免同一時間段重複預約），改為接收 ReservationAndTableReq
	@Override
	public ReservationAndTableRes checkIfTableCanBeBooked(ReservationAndTableReq req) {
	    // 從 req 中提取需要的字段
	    TableManagement table = req.getTable(); // 取得 TableManagement 對象
	    String tableNumber = table.getTableNumber(); // 從 TableManagement 對象中取得桌位號碼
	    LocalDate reservationDate = req.getReservationDate();
	    LocalTime reservationStartTime = req.getReservationStartTime();
	    LocalTime reservationEndTime = req.getReservationEndTime();

	    // 1. 驗證 tableNumber 是否為 null 或空
	    if (tableNumber == null || tableNumber.trim().isEmpty()) {
	        return new ReservationAndTableRes(400, "無效的桌位號碼，不能為空");
	    }

	    // 調用私有方法進行輸入驗證
	    validateReservationInput(reservationDate, reservationStartTime, reservationEndTime);

	    // 2. 根據傳入的日期獲取當天的營業時間
	    java.time.DayOfWeek javaDayOfWeek = reservationDate.getDayOfWeek();
	    OperatingHours.DayOfWeek dayOfWeek = convertToOperatingHoursDayOfWeek(javaDayOfWeek);

	    // 3. 從資料庫中獲取當天的營業時間、用餐時長和清潔間隔
	    List<Object[]> hours = operatingHoursDao.getOperatingHours(dayOfWeek);

	    if (hours.isEmpty()) {
	        return new ReservationAndTableRes(400, "當天無營業時間設定");
	    }

	    for (Object[] hour : hours) {
	        LocalTime openingTime = (LocalTime) hour[0];
	        LocalTime closingTime = (LocalTime) hour[1];
	        int diningDuration = (int) hour[2];
	        Integer cleaningBreak = (Integer) hour[3]; // 獲取 cleaningBreak，可以為 null

	        // 4. 動態計算可用時間段
	        List<LocalTime> availableSlots = operatingHoursService.calculateAvailableTimeSlots(
	            openingTime, closingTime, diningDuration, cleaningBreak);

	        // 5. 檢查用戶選擇的預約時間段是否在可用的時間段內
	        if (!availableSlots.contains(reservationStartTime)) {
	            return new ReservationAndTableRes(400, "所選時間段無法進行預約，請選擇其他時間段");
	        }

	        // 6. 查詢該桌位在此日期的其他預約，並檢查其狀態
	        List<ReservationAndTable> existingReservations = reservationAndTableDao.findByTableNumberAndReservationDate(tableNumber, reservationDate);

	        // 7. 檢查時間段內是否有重疊的預約，並根據桌位狀態判斷是否允許新的預約
	        for (ReservationAndTable reservationAndTable : existingReservations) {
	            LocalTime existingStartTime = reservationAndTable.getReservationStartTime();
	            LocalTime existingEndTime = reservationAndTable.getReservationEndTime();
	            TableStatus currentStatus = reservationAndTable.getTableStatus(); // 取得桌位的當前狀態

	            // 如果時間段有重疊，並且桌位當前的狀態是 "用餐中" 或 "訂位中"，則不允許預約
	            if (reservationStartTime.isBefore(existingEndTime) && reservationEndTime.isAfter(existingStartTime)) {
	                if (currentStatus == TableStatus.用餐中 || currentStatus == TableStatus.訂位中) {
	                    return new ReservationAndTableRes(400, "該時間段桌位已被預約或正在用餐，無法再次預約");
	                }
	            }
	        }
	    }

	    // 沒有衝突，允許預約
	    return new ReservationAndTableRes(200, "該桌位可以預約");
	}
    
	// 4. 查詢特定日期內所有未被預約的空閒桌位，改為接收 ReservationAndTableReq
	@Override
	public List<TableManagement> getAvailableTables(ReservationAndTableReq req) {
	    // 從 req 中提取需要的字段
	    LocalDate reservationDate = req.getReservationDate();

	    // 驗證輸入的預約參數（僅檢查日期）
	    if (reservationDate == null) {
	        throw new IllegalArgumentException("預約日期不能為空");
	    }

	    // 取得對應日期的營業時間
	    java.time.DayOfWeek javaDayOfWeek = reservationDate.getDayOfWeek();
	    OperatingHours.DayOfWeek dayOfWeek = convertToOperatingHoursDayOfWeek(javaDayOfWeek);

	    // 從資料庫獲取當天的營業時間
	    List<Object[]> hours = operatingHoursDao.getOperatingHours(dayOfWeek);
	    if (hours.isEmpty()) {
	        // 返回一個空的列表，而不是拋出異常
	        return Collections.emptyList();
	    }

	    // 查詢當天已被預約的桌位及其狀態
	    List<ReservationAndTable> reservedTablesAndStatus = reservationAndTableDao.findReservedTablesByDate(reservationDate);

	    // 當沒有預約時，直接從 table_management 中查詢所有狀態為 "可使用" 的桌位
	    if (reservedTablesAndStatus.isEmpty()) {
	        // 從 table_management 查詢所有狀態為 "可使用" 的桌位
	        return tableManagementDao.findByTableStatus(TableManagement.TableStatus.可使用);
	    }

	    // 過濾出所有未被預約並且狀態為 "可使用" 的桌位
	    List<TableManagement> allTables = tableManagementDao.findAll();
	    List<TableManagement> availableTables = allTables.stream()
	        .filter(table -> reservedTablesAndStatus.stream()
	            .noneMatch(rt -> rt.getTable().equals(table) &&
	                (rt.getTableStatus() == TableStatus.訂位中 || rt.getTableStatus() == TableStatus.用餐中)))  // 排除狀態為 "訂位中" 或 "用餐中" 的桌位
	        .collect(Collectors.toList());

	    // 如果無可用桌位，返回空列表
	    if (availableTables.isEmpty()) {
	        return Collections.emptyList(); // 返回空列表
	    }

	    // 返回可用的桌位列表
	    return availableTables;
	}

    // 5. 自動分配桌位
	@Override
	public ReservationAndTableRes autoAssignTable(ReservationAndTableReq req) {
	    int reservationPeople = req.getReservationPeople();
	    LocalDate reservationDate = req.getReservationDate();
	    LocalTime reservationStartTime = req.getReservationStartTime();
	    LocalTime reservationEndTime = req.getReservationEndTime();

	    // 1. 驗證輸入參數
	    if (reservationPeople <= 0) {
	        return new ReservationAndTableRes(400, "預約人數必須大於 0");
	    }

	    // 調用私有方法進行輸入驗證
	    validateReservationInput(reservationDate, reservationStartTime, reservationEndTime);

	    // 2. 查詢所有在該時間段可用的桌位
	    List<TableManagement> availableTables = getAvailableTables(req);

	    // 3. 驗證是否有可用桌位
	    if (availableTables.isEmpty()) {
	        return new ReservationAndTableRes(404, "該時間段內無可用桌位");
	    }

	    // 儲存已選擇的桌位
	    List<TableManagement> selectedTables = new ArrayList<>();

	    // 儲存完全匹配的桌位
	    List<TableManagement> exactMatchTables = new ArrayList<>();

	    // 儲存比需求人數大的桌位
	    List<TableManagement> largerTables = new ArrayList<>();

	    // 儲存可以用來合併的桌位
	    List<TableManagement> mergeableTables = new ArrayList<>();

	    // 4. 將桌位分為三類
	    for (TableManagement table : availableTables) {
	        if (table.getTableCapacity() == reservationPeople) {
	            exactMatchTables.add(table); // 優先完全匹配的桌位
	        } else if (table.getTableCapacity() > reservationPeople) {
	            largerTables.add(table); // 容量大於需求人數
	        } else {
	            mergeableTables.add(table); // 容量小於需求人數的桌位用來合併
	        }
	    }

	    int remainingPeople = reservationPeople;

	    // 5. 優先選擇完全匹配的桌位
	    if (!exactMatchTables.isEmpty()) {
	        TableManagement exactTable = exactMatchTables.get(0); // 取第一張完全匹配的桌位
	        selectedTables.add(exactTable);
	        remainingPeople = 0; // 人數已滿足
	    }

	    // 6. 若沒有完全匹配的桌位，分配容量大於需求的桌位
	    if (remainingPeople > 0 && !largerTables.isEmpty()) {
	        TableManagement bestFitTable = largerTables.stream()
	                .min(Comparator.comparingInt(TableManagement::getTableCapacity))
	                .orElse(null); // 選擇最接近人數的較大桌位
	        if (bestFitTable != null) {
	            selectedTables.add(bestFitTable);
	            remainingPeople = 0; // 已經滿足需求人數
	        }
	    }

	    // 7. 如果仍有剩餘人數，則進行併桌
	    if (remainingPeople > 0) {
	        mergeableTables.sort(Comparator.comparingInt(TableManagement::getTableCapacity)); // 併桌根據容量排序
	        for (TableManagement table : mergeableTables) {
	            selectedTables.add(table);
	            remainingPeople -= table.getTableCapacity();

	            // 如果已經滿足或超過需求人數，停止分配
	            if (remainingPeople <= 0) {
	                break;
	            }
	        }
	    }

	    // 8. 如果仍有剩餘人數，表示無法分配合適的桌位
	    if (remainingPeople > 0) {
	        return new ReservationAndTableRes(400, "無法找到合適的桌位進行分配");
	    }

	    // 9. 封裝結果並返回成功的響應
	    ReservationAndTableRes response = new ReservationAndTableRes(200, "成功分配桌位");
	    List<String> tableNumbers = selectedTables.stream()
	                                .map(TableManagement::getTableNumber)
	                                .collect(Collectors.toList());
	    response.setTableNumbers(tableNumbers);

	    return response;
	}
	
	
}