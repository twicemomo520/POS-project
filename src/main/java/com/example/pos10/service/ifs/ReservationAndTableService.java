package com.example.pos10.service.ifs;

import java.util.List;

import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.entity.ReservationAndTable.TableStatus;
import com.example.pos10.vo.ReservationAndTableReq;
import com.example.pos10.vo.ReservationAndTableRes;

public interface ReservationAndTableService {

	// 1. 查詢與預約或桌位狀態關聯的所有桌位列表
	public List <TableManagement> getTablesForReservation (int reservationId, List <TableStatus> statuses);

    // 2. 查詢與桌位相關的所有預約
	public List <Reservation> getReservationsForTable (String tableNumber, List <TableStatus> statuses);
    
    // 3. 檢查桌位是否可以在指定時間段內被預約（避免同一時間段重複預約）
    public ReservationAndTableRes checkIfTableCanBeBooked (ReservationAndTableReq req);

    // 4. 查詢特定日期和時間段內所有未被預約的空閒桌位
    public List <TableManagement> getAvailableTables (ReservationAndTableReq req);
    
    // 5. 自動分配桌位
    public ReservationAndTableRes autoAssignTable (ReservationAndTableReq req);
}