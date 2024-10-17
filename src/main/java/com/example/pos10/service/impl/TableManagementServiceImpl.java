package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.OperatingHours;
import com.example.pos10.entity.OperatingHours.DayOfWeek;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.entity.TableManagement.TableStatus;
import com.example.pos10.repository.OperatingHoursDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.OperatingHoursService;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;
import com.example.pos10.vo.TimeSlotWithTableStatusRes;

@Service
public class TableManagementServiceImpl implements TableManagementService {

    @Autowired
    private TableManagementDao tableManagementDao;
    
    @Autowired
    private OperatingHoursDao operatingHoursDao;
    
    @Autowired
    private OperatingHoursService operatingHoursService;

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
    
 // 5. 根據時間段獲取所有桌位的狀態
    @Override
    public List<TimeSlotWithTableStatusRes> getTodayTableStatuses() {
        LocalDate today = LocalDate.now(); // 獲取今天的日期
        DayOfWeek dayOfWeek;

        // 根據今天的星期幾設定對應的 DayOfWeek 枚舉
        switch (today.getDayOfWeek()) {
            case MONDAY:
                dayOfWeek = DayOfWeek.星期一;
                break;
            case TUESDAY:
                dayOfWeek = DayOfWeek.星期二;
                break;
            case WEDNESDAY:
                dayOfWeek = DayOfWeek.星期三;
                break;
            case THURSDAY:
                dayOfWeek = DayOfWeek.星期四;
                break;
            case FRIDAY:
                dayOfWeek = DayOfWeek.星期五;
                break;
            case SATURDAY:
                dayOfWeek = DayOfWeek.星期六;
                break;
            case SUNDAY:
                dayOfWeek = DayOfWeek.星期日;
                break;
            default:
                throw new IllegalArgumentException("無法獲取星期幾");
        }

        // 從資料庫中獲取當天的營業時間和用餐時間
        List<Object[]> hours = operatingHoursDao.getOperatingHours(dayOfWeek);
        
        if (hours.isEmpty()) {
            throw new IllegalArgumentException("當天無營業時間設定");
        }
        
        List<TimeSlotWithTableStatusRes> response = new ArrayList<>();

        // 遍歷所有營業時間
        for (Object[] hour : hours) {
            LocalTime openingTime = (LocalTime) hour[0];
            LocalTime closingTime = (LocalTime) hour[1];
            int diningDuration = (int) hour[2]; // 用餐時間從資料庫中獲取

            // 計算可用的預約時間段
            List<LocalTime> availableSlots = operatingHoursService.calculateAvailableTimeSlots(openingTime, closingTime, diningDuration);
            
            // 查詢所有桌位的狀態
            List<TableManagement> allTables = tableManagementDao.findAll();

            // 對每個可用時間段進行處理
            for (LocalTime slot : availableSlots) {
                LocalTime nextSlot = slot.plusMinutes(diningDuration);
                
                // 創建當前時間段的桌位狀態
                TimeSlotWithTableStatusRes slotStatus = new TimeSlotWithTableStatusRes(
                    slot + " - " + nextSlot,
                    allTables // 在這裡使用所有桌位的狀態
                );
                
                // 將時間段與桌位狀態添加到回應列表
                response.add(slotStatus);
                
                // 打印當前時間段的桌位狀態
                System.out.println("時間段: " + slot + " - " + nextSlot + " 的桌位狀態: " + allTables);
            }
        }

        return response; // 返回包含時間段和桌位狀態的列表
    }

    // 6. 根據時間段獲取所有可用的桌位狀態
    @Override
    public List<TimeSlotWithTableStatusRes> getAvailableTableStatusesByDate(LocalDate reservationDate) {
     // 根據傳入的日期獲取星期幾
     java.time.DayOfWeek javaDayOfWeek = reservationDate.getDayOfWeek();
     OperatingHours.DayOfWeek dayOfWeek;

     switch (javaDayOfWeek) {
     case MONDAY:
      dayOfWeek = OperatingHours.DayOfWeek.星期一;
      break;
     case TUESDAY:
      dayOfWeek = OperatingHours.DayOfWeek.星期二;
      break;
     case WEDNESDAY:
      dayOfWeek = OperatingHours.DayOfWeek.星期三;
      break;
     case THURSDAY:
      dayOfWeek = OperatingHours.DayOfWeek.星期四;
      break;
     case FRIDAY:
      dayOfWeek = OperatingHours.DayOfWeek.星期五;
      break;
     case SATURDAY:
      dayOfWeek = OperatingHours.DayOfWeek.星期六;
      break;
     case SUNDAY:
      dayOfWeek = OperatingHours.DayOfWeek.星期日;
      break;
     default:
      throw new IllegalArgumentException("無法獲取星期幾");
     }

     // 從資料庫中獲取當天的營業時間和用餐時間
     List<Object[]> hours = operatingHoursDao.getOperatingHours(dayOfWeek);

     if (hours.isEmpty()) {
      throw new IllegalArgumentException("當天無營業時間設定");
     }

     List<TimeSlotWithTableStatusRes> response = new ArrayList<>();

     // 遍歷所有營業時間
     for (Object[] hour : hours) {
      LocalTime openingTime = (LocalTime) hour[0];
      LocalTime closingTime = (LocalTime) hour[1];
      int diningDuration = (int) hour[2]; // 用餐時間從資料庫中獲取

      // 計算可用的預約時間段
      List<LocalTime> availableSlots = operatingHoursService.calculateAvailableTimeSlots(openingTime, closingTime,
        diningDuration);

      // 對每個可用時間段進行處理
      for (LocalTime slot : availableSlots) {
       LocalTime nextSlot = slot.plusMinutes(diningDuration);

       // 查詢可用桌位
       List<TableManagement> availableTables = tableManagementDao
         .findAvailableTablesInTimeSlot(reservationDate, slot, nextSlot);

       // 創建當前時間段的桌位狀態
       TimeSlotWithTableStatusRes slotStatus = new TimeSlotWithTableStatusRes(slot + " - " + nextSlot,
         availableTables // 在這裡使用可用的桌位狀態
       );

       // 將時間段與桌位狀態添加到回應列表
       response.add(slotStatus);

       // 打印當前時間段的桌位狀態
       System.out.println("時間段: " + slot + " - " + nextSlot + " 的桌位狀態: " + availableTables);
      }
     }

     return response; // 返回包含時間段和可用桌位狀態的列表
    }
    
    // 7. 根據日期和訂位時間查詢可使用的桌位狀態
    @Override
    public List<TimeSlotWithTableStatusRes> getAvailableTableStatuses(LocalDate reservationDate, LocalTime reservationStartTime, LocalTime reservationEndingTime) {
        // 根據傳入的日期獲取星期幾
        java.time.DayOfWeek javaDayOfWeek = reservationDate.getDayOfWeek();
        OperatingHours.DayOfWeek dayOfWeek;

        switch (javaDayOfWeek) {
            case MONDAY:
                dayOfWeek = OperatingHours.DayOfWeek.星期一;
                break;
            case TUESDAY:
                dayOfWeek = OperatingHours.DayOfWeek.星期二;
                break;
            case WEDNESDAY:
                dayOfWeek = OperatingHours.DayOfWeek.星期三;
                break;
            case THURSDAY:
                dayOfWeek = OperatingHours.DayOfWeek.星期四;
                break;
            case FRIDAY:
                dayOfWeek = OperatingHours.DayOfWeek.星期五;
                break;
            case SATURDAY:
                dayOfWeek = OperatingHours.DayOfWeek.星期六;
                break;
            case SUNDAY:
                dayOfWeek = OperatingHours.DayOfWeek.星期日;
                break;
            default:
                throw new IllegalArgumentException("無法獲取星期幾");
        }

        // 從資料庫中獲取當天的營業時間和用餐時間
        List<Object[]> hours = operatingHoursDao.getOperatingHours(dayOfWeek);

        if (hours.isEmpty()) {
            throw new IllegalArgumentException("當天無營業時間設定");
        }

        List<TimeSlotWithTableStatusRes> response = new ArrayList<>();

        // 遍歷所有營業時間
        for (Object[] hour : hours) {
            LocalTime openingTime = (LocalTime) hour[0];
            LocalTime closingTime = (LocalTime) hour[1];
            int diningDuration = (int) hour[2]; // 用餐時間從資料庫中獲取

            // 計算可用的預約時間段
            List<LocalTime> availableSlots = operatingHoursService.calculateAvailableTimeSlots(openingTime, closingTime, diningDuration);

            // 遍歷每個可用時間段，過濾出符合當前預約時間的可用時間段
            for (LocalTime slot : availableSlots) {
                LocalTime nextSlot = slot.plusMinutes(diningDuration);

                // 優化後的重疊檢查邏輯
                if (!nextSlot.isBefore(reservationStartTime) && !slot.isAfter(reservationEndingTime)) {
                    // 查詢該時間段內的可用桌位
                    List<TableManagement> availableTables = tableManagementDao.findAvailableTablesInTimeSlot(reservationDate, slot, nextSlot);

                    // 創建當前時間段的桌位狀態
                    TimeSlotWithTableStatusRes slotStatus = new TimeSlotWithTableStatusRes(
                        slot + " - " + nextSlot,
                        availableTables // 使用該時間段內的可用桌位狀態
                    );

                    // 將時間段與桌位狀態添加到回應列表
                    response.add(slotStatus);

                    // 打印當前時間段的桌位狀態
                    System.out.println("時間段: " + slot + " - " + nextSlot + " 的桌位狀態: " + availableTables);
                }
            }
        }

        return response; // 返回包含時間段和可用桌位狀態的列表
    }
  }