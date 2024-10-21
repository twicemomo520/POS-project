package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.OperatingHours;
import com.example.pos10.entity.OperatingHours.DayOfWeek;
import com.example.pos10.entity.ReservationAndTable;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.OperatingHoursDao;
import com.example.pos10.repository.ReservationAndTableDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.OperatingHoursService;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;
import com.example.pos10.vo.TableStatusRes;
import com.example.pos10.vo.TimeSlotWithTableStatusRes;

@Service
public class TableManagementServiceImpl implements TableManagementService {

    @Autowired
    private TableManagementDao tableManagementDao;
    
    @Autowired
    private OperatingHoursDao operatingHoursDao;
    
    @Autowired
    private OperatingHoursService operatingHoursService;
    
    @Autowired
    private ReservationAndTableDao reservationAndTableDao;
    
    // 轉換方法，將 Java 的 DayOfWeek 轉換為自定義的 OperatingHours.DayOfWeek
    private OperatingHours.DayOfWeek convertDayOfWeek(java.time.DayOfWeek javaDayOfWeek) {
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
                throw new IllegalArgumentException("無效的星期: " + javaDayOfWeek);
        }
    }

    // 1. 創建桌位
    @Override
    @Transactional
    public TableManagementRes createTable(TableManagementReq tableReq) {
        // 1. 檢查桌號是否已經存在
        if (tableManagementDao.existsById(tableReq.getTableNumber())) {
            return new TableManagementRes(400, "這個桌號已經存在在資料庫中 !!!");
        }

        // 2. 檢查桌位容納人數是否合理
        if (tableReq.getTableCapacity() <= 0) {
            return new TableManagementRes(400, "桌位容納人數必須要大於 0 !!!");
        }

        // 3. 檢查桌位狀態是否為空
        if (tableReq.getTableStatus() == null) {
            return new TableManagementRes(400, "桌位狀態不得為空或 null !!!");
        }

        // 4. 檢查桌號格式是否正確（根據你的需求自定義格式）
        if (!tableReq.getTableNumber().matches("[A-Z]\\d{2}")) {
            return new TableManagementRes(400, "桌號必須是一個大寫字母加兩個數字（如 A01）!!!");
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
    public TableManagementRes deleteTable (String tableNumber) {
        // 1. 檢查桌號是否存在
        TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
        if (table == null) {
            return new TableManagementRes(400, "找不到相關的桌號 !!!");
        }

        // 2. 檢查桌位是否與 ReservationAndTable 相關聯
        if (table.getReservationTables() != null && !table.getReservationTables().isEmpty()) {
            return new TableManagementRes(400, "這個桌號有相關的訂位不能刪除 !!!");
        }

        // 3. 檢查桌位狀態是否允許刪除（例如桌位必須是 可使用 狀態）
        if (!table.getTableStatus().equals(TableManagement.TableStatus.可使用)) {
            return new TableManagementRes(400, "桌位狀態必須是可使用才能刪除 !!!");
        }

        // 4. 執行刪除操作
        tableManagementDao.deleteById(tableNumber);
        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }
    
    // 3. 更新桌位編號或容納人數
    @Override
    @Transactional
    public TableManagementRes updateTable (String oldTableNumber, String newTableNumber, Integer newCapacity) {
        // 1. 檢查舊的桌號是否存在
        TableManagement table = tableManagementDao.findById(oldTableNumber).orElse(null);
        if (table == null) {
            return new TableManagementRes(400, "找不到相關的桌號 !!!");
        }

        // 2. 更新容納人數（如果有提供新的容納人數）
        if (newCapacity != null && newCapacity > 0) {
            table.setTableCapacity(newCapacity);
        }

        // 3. 如果有新的桌號，檢查新桌號是否已存在
        if (newTableNumber != null && !newTableNumber.isEmpty() && !newTableNumber.equals(oldTableNumber)) {
            if (tableManagementDao.existsById(newTableNumber)) {
                return new TableManagementRes(400, "這個桌號已經存在在資料庫中 !!!");
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
    public List <TableManagement> getAllTables () {
        List <TableManagement> allTables = tableManagementDao.findAll();

        // 檢查是否有桌位資料
        if (allTables.isEmpty()) {
            throw new IllegalArgumentException("沒有可使用中的桌位 !!!");
        }

        return allTables;
    }
    
    // 5. 根據時間段獲取所有桌位的狀態
    @Override
    public List<TimeSlotWithTableStatusRes> getTodayTableStatuses() {
        LocalDate today = LocalDate.now();
        
        // 使用 Java 內建方法取得今天是星期幾，並轉換為自定義的 Enum
        OperatingHours.DayOfWeek dayOfWeek = convertDayOfWeek(today.getDayOfWeek());
        
        // 根據星期幾查詢營業時間
        List<Object[]> hours = operatingHoursDao.getOperatingHours(dayOfWeek);

        if (hours.isEmpty()) {
            throw new IllegalArgumentException("當天無營業時間設定");
        }

        List<TimeSlotWithTableStatusRes> response = new ArrayList<>();
        List<TableManagement> allTables = tableManagementDao.findAll(); // 獲取所有桌位資料

        for (Object[] hour : hours) {
            LocalTime openingTime = (LocalTime) hour[0];
            LocalTime closingTime = (LocalTime) hour[1];
            int diningDuration = (int) hour[2];
            Integer cleaningBreak = (Integer) hour[3];

            List<LocalTime> availableSlots = operatingHoursService.calculateAvailableTimeSlots(
                    openingTime, closingTime, diningDuration, cleaningBreak);

            for (LocalTime slotStart : availableSlots) {
                LocalTime slotEnd = slotStart.plusMinutes(diningDuration);

                // 打印當前時間段和日期
                System.out.println("當前日期: " + today);
                System.out.println("當前時間段: " + slotStart + " - " + slotEnd);

                // 查詢當前時間段的所有預約
                List<ReservationAndTable> reservationsInSlot = reservationAndTableDao
                        .findReservationsInTimeSlot(today, slotStart, slotEnd);

                System.out.println("找到的預約數量: " + reservationsInSlot.size());

                List<TableStatusRes> tableStatusesForSlot = new ArrayList<>();

                for (TableManagement table : allTables) {
                    // 檢查當前桌位是否有對應的預約
                    List<ReservationAndTable> reservationsForTable = reservationsInSlot.stream()
                            .filter(r -> r.getTable().getTableNumber().equals(table.getTableNumber()))
                            .collect(Collectors.toList());

                    System.out.println("桌位: " + table.getTableNumber() + " 找到的預約數量: " + reservationsForTable.size());

                    // 如果有預約，將桌位狀態設為已訂位
                    if (!reservationsForTable.isEmpty()) {
                        table.setTableStatus(TableManagement.TableStatus.訂位中);
                        table.setReservationTables(reservationsForTable);
                    } else {
                        table.setTableStatus(TableManagement.TableStatus.可使用);
                        table.setReservationTables(Collections.emptyList());
                    }

                    tableStatusesForSlot.add(new TableStatusRes(table.getTableNumber(),
                            table.getTableCapacity(), table.getTableStatus(), table.getReservationTables()));
                }

                // 將當前時間段與對應的桌位狀態添加到返回結果中
                response.add(new TimeSlotWithTableStatusRes(slotStart + " - " + slotEnd, tableStatusesForSlot));
            }
        }

        return response;
    }
}