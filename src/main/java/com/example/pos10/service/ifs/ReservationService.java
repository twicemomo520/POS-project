package com.example.pos10.service.ifs;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

public interface ReservationService {
	
	// 1. 查詢可用時間段
	public ReservationRes findAvailableTimeSlots(LocalDate reservationDate, int reservationPeople);
    
    // 2. 儲存訂位
    public ReservationRes saveReservation (ReservationReq reservationReq);

    // 3. 根據顧客電話號碼查詢訂位
    public ReservationRes findReservationsByPhoneNumber (String phoneNumber);

    // 4. 查詢當天的所有訂位
    public ReservationRes findReservationsByDate (LocalDate reservationDate);

    // 5. 手動取消訂位
    public ReservationRes cancelReservation (int reservationId);

    // 6. 自動更新桌位狀態（每10分鐘執行一次）
    public ReservationRes autoUpdateTableStatus (LocalDate currentDate, LocalTime currentTime);

    // 7. 手動報到更新桌位狀態
    public ReservationRes manualCheckIn (String tableNumber, int reservationId);
    
    // 8. 訂位前一天發送提醒
    public void sendReservationReminders ();
}