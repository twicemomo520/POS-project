package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.OperatingHours;
import com.example.pos10.entity.ReservationAndTableTimeslot;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.OperatingHoursDao;
import com.example.pos10.repository.ReservationAndTableTimeslotDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.TableManagementReq;
import com.example.pos10.vo.TableManagementRes;

@Service
public class TableManagementServiceImpl implements TableManagementService {

    @Autowired
    private TableManagementDao tableManagementDao;
    
    @Autowired
    private OperatingHoursDao operatingHoursDao;
    
    @Autowired
    private ReservationAndTableTimeslotDao reservationAndTableTimeslotDao;
    
    // 1. 創建桌位
    @Override
    @Transactional
    public TableManagementRes createTable(TableManagementReq tableReq) {
        // 檢查桌號是否已經存在
        if (tableManagementDao.existsById(tableReq.getTableNumber())) {
            return new TableManagementRes(400, "這個桌號已經存在在資料庫中 !!!");
        }

        // 檢查桌位容納人數是否合理
        if (tableReq.getTableCapacity() <= 0) {
            return new TableManagementRes(400, "桌位容納人數必須要大於 0 !!!");
        }

        // 檢查桌號格式是否正確
        if (!tableReq.getTableNumber().matches("[A-Z]\\d{2}")) {
            return new TableManagementRes(400, "桌號必須是一個大寫字母加兩個數字（如 A01）!!!");
        }

        // 插入新桌位到 table_management 表
        tableManagementDao.insertTableNumber(
            tableReq.getTableNumber(), 
            tableReq.getTableCapacity()
        );

        // 從 operating_hours 表查詢所有營業時間段
        List<OperatingHours> operatingHoursList = operatingHoursDao.findAll();

        // 將新桌位與每個營業時間段關聯，插入 reservation_and_table_timeslot 表
        for (OperatingHours hours : operatingHoursList) {
            reservationAndTableTimeslotDao.insertTimeslot(
                tableReq.getTableNumber(),
                hours.getId(), // operating_hours 表中的 ID
                LocalDate.now(), // 或者你可以使用更動態的日期邏輯
                "可使用" // 默認狀態
            );
        }

        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }
    
    // 2. 刪除桌位
    @Override
    @Transactional
    public TableManagementRes deleteTable(String tableNumber) {
        // 1. 檢查桌號是否存在
        TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
        if (table == null) {
            return new TableManagementRes(400, "找不到相關的桌號 !!!");
        }

        // 2. 檢查桌位是否在 reservation_and_table_timeslot 中被使用
        List<ReservationAndTableTimeslot> relatedReservations = reservationAndTableTimeslotDao.findByTableNumber(tableNumber);

        if (relatedReservations != null && !relatedReservations.isEmpty()) {
            // 檢查是否有相關的桌位狀態不是 "可使用"
            for (ReservationAndTableTimeslot timeslot : relatedReservations) {
                if (!timeslot.getTableStatus().equals(ReservationAndTableTimeslot.TableStatus.可使用)) {
                    return new TableManagementRes(400, "這個桌號目前無法刪除，因為它處於非可使用狀態（如訂位中或用餐中）!!!");
                }
            }

            // 3. 刪除 reservation_and_table_timeslot 中的相關記錄
            reservationAndTableTimeslotDao.deleteByTableNumber(tableNumber);
        }

        // 4. 刪除 table_management 表中的桌位記錄
        try {
            tableManagementDao.deleteById(tableNumber);
        } catch (Exception e) {
            return new TableManagementRes(500, "刪除桌位時發生錯誤，請稍後再試: " + e.getMessage());
        }

        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }
    
    // 3. 更新桌位編號或容納人數
    @Override
    @Transactional
    public TableManagementRes updateTable(String oldTableNumber, String newTableNumber, Integer newCapacity) {
        // 1. 檢查舊的桌號是否存在
        TableManagement table = tableManagementDao.findById(oldTableNumber).orElse(null);
        if (table == null) {
            return new TableManagementRes(400, "找不到相關的桌號 !!!");
        }

        // 2. 更新容納人數（如果有提供新的容納人數）
        if (newCapacity != null && newCapacity > 0 && !newCapacity.equals(table.getTableCapacity())) {
            table.setTableCapacity(newCapacity);
        }

        // 3. 如果有新的桌號，檢查新桌號是否已存在
        if (newTableNumber != null && !newTableNumber.isEmpty() && !newTableNumber.equals(oldTableNumber)) {
            if (tableManagementDao.existsById(newTableNumber)) {
                return new TableManagementRes(400, "這個桌號已經存在在資料庫中 !!!");
            }

            // 4. 刪除 reservation_and_table_timeslot 中的相關記錄
            reservationAndTableTimeslotDao.deleteByTableNumber(oldTableNumber);

            // 5. 刪除舊的桌號
            tableManagementDao.deleteById(oldTableNumber);

            // 6. 創建新桌號並保存
            TableManagement newTable = new TableManagement(newTableNumber, table.getTableCapacity());
            tableManagementDao.save(newTable);

            // 7. 從 operating_hours 表查詢所有營業時間段
            List<OperatingHours> operatingHoursList = operatingHoursDao.findAll();

            // 8. 將新桌位與每個營業時間段關聯，插入 reservation_and_table_timeslot 表
            for (OperatingHours hours : operatingHoursList) {
                reservationAndTableTimeslotDao.insertTimeslot(
                    newTableNumber,
                    hours.getId(), // operating_hours 表中的 ID
                    LocalDate.now(), // 或者你可以使用更動態的日期邏輯
                    "可使用" // 默認狀態
                );
            }

            return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
        }

        // 9. 保存更新的桌位（如果只更新了容納人數）
        tableManagementDao.save(table);
        return new TableManagementRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 4. 獲取所有桌位
    @Override
    public List<TableManagement> getAllTables() {
        List<TableManagement> allTables = tableManagementDao.findAll();

        // 如果沒有桌位資料，返回空列表，避免丟擲異常
        if (allTables.isEmpty()) {
            return Collections.emptyList();
        }

        return allTables;
    }
}