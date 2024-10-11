package com.example.pos10.service.ifs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

public interface ReservationService {
	
	// 1. 查詢可用桌位並儲存
	public ReservationRes generateAndFindAvailableTables(LocalDate reservationDate, int diningDuration, int reservationPeople);

	// 2. 計算可用的開始時間段
	public List<LocalTime> calculateAvailableStartTimes(LocalTime openingTime, LocalTime closingTime, int diningDuration);
    
    // 3. 儲存訂位
    public ReservationRes saveReservation (ReservationReq reservationReq);
//
//    // 2. 根據顧客電話號碼查詢訂位
//    public ReservationRes findReservationsByPhoneNumber (String phoneNumber);

    // 3. 查詢當天的所有訂位
    public ReservationRes findReservationsByDate (LocalDate reservationDate);

    // 4. 取消訂位
    public ReservationRes cancelReservation (int reservationId);
//
//    // 5. 自動更新桌位狀態
//    public void autoUpdateTableStatus (LocalDate currentDate, LocalTime currentTime, LocalTime cutOffTime);
//    
//    // 6. 訂位前一天發送提醒
//    public void sendReservationReminders ();
//    
}