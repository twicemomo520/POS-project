package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // 1. 儲存訂位
    @PostMapping (value = "/reservation/saveReservation")
    public ReservationRes saveReservation(@RequestBody ReservationReq reservationReq) {
        return reservationService.saveReservation(reservationReq);
    }

    // 2. 根據顧客電話號碼查詢訂位
    @GetMapping (value = "/reservation/findReservationsByPhoneNumber")
    public ReservationRes findReservationsByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return reservationService.findReservationsByPhoneNumber(phoneNumber);
    }

    // 3. 查詢當天的所有訂位
    @GetMapping (value = "/reservation/findReservationsByDate")
    public ReservationRes findReservationsByDate (@RequestParam("date") String date) {
        LocalDate reservationDate = LocalDate.parse(date); // 轉換字串為 LocalDate
        return reservationService.findReservationsByDate(reservationDate);
    }

    // 4. 取消訂位
    @PostMapping (value = "/reservation/cancelReservation")
    public ReservationRes cancelReservation (@RequestParam("tableNumber") String tableNumber) {
        return reservationService.cancelReservation(tableNumber);
    }

    // 5. 手動觸發自動更新桌位狀態（供測試用途）
    @PostMapping (value = "/reservation/autoUpdateTableStatus")
    public void autoUpdateTableStatus () {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime cutOffTime = currentTime.minusMinutes(10);
        reservationService.autoUpdateTableStatus(currentDate, currentTime, cutOffTime);
    }
}