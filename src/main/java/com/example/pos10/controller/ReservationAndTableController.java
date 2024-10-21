package com.example.pos10.controller;

import com.example.pos10.service.ifs.ReservationAndTableService;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.ReservationAndTable.TableStatus;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.vo.ReservationAndTableReq;
import com.example.pos10.vo.ReservationAndTableRes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation-table")
public class ReservationAndTableController {

    @Autowired
    private ReservationAndTableService reservationAndTableService;

    // 1. 查詢與預約或桌位狀態關聯的所有桌位列表
    @GetMapping(value = "/getTablesForReservation/{reservationId}")
    public List<TableManagement> getTablesForReservation(
            @PathVariable int reservationId,
            @RequestParam(required = false) List<TableStatus> statuses) {

        // 如果沒有提供桌位狀態，傳入 null，表示查詢所有狀態的桌位
        if (statuses == null || statuses.isEmpty()) {
            statuses = null; // 傳入 null 表示不過濾狀態
        }

        return reservationAndTableService.getTablesForReservation(reservationId, statuses);
    }

    // 2. 查詢與桌位相關的所有預約
    @GetMapping("/getReservationsForTable/{tableNumber}")
    public List<Reservation> getReservationsForTable(
            @PathVariable String tableNumber,
            @RequestParam(required = false) List<TableStatus> statuses) {

        // 如果沒有提供桌位狀態，傳入 null，表示查詢所有狀態的預約
        if (statuses == null || statuses.isEmpty()) {
            statuses = null; // 傳入 null 表示不過濾狀態
        }

        return reservationAndTableService.getReservationsForTable(tableNumber, statuses);
    }

    // 3. 檢查桌位是否可以在指定時間段內被預約
    @PostMapping("/check-table-availability")
    public ReservationAndTableRes checkIfTableCanBeBooked(@RequestBody ReservationAndTableReq req) {
        return reservationAndTableService.checkIfTableCanBeBooked(req);
    }

    // 4. 查詢特定日期和時間段內所有未被預約的空閒桌位
    @PostMapping("/available-tables")
    public List<TableManagement> getAvailableTables(@RequestBody ReservationAndTableReq req) {
        return reservationAndTableService.getAvailableTables(req);
    }

    // 5. 自動分配桌位
    @PostMapping("/auto-assign-table")
    public ReservationAndTableRes autoAssignTable(@RequestBody ReservationAndTableReq req) {
        return reservationAndTableService.autoAssignTable(req);
    }
}