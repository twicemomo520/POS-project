package com.example.pos10.service.ifs;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

public interface ReservationService {
    
    // 1. 儲存訂位
    public ReservationRes saveReservation (ReservationReq reservationReq);

    // 2. 根據顧客電話號碼查詢訂位
    public ReservationRes findReservationsByPhoneNumber (String phoneNumber);

    // 3. 查詢當天的所有訂位
    public ReservationRes findReservationsByDate (LocalDate reservationDate);

    // 4. 取消訂位
    public ReservationRes cancelReservation (String tableNumber);

    // 5. 自動更新桌位狀態
    public void autoUpdateTableStatus (LocalDate currentDate, LocalTime currentTime, LocalTime cutOffTime);
}