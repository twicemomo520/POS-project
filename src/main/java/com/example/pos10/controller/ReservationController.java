package com.example.pos10.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

@RestController
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    
    // 1. 查詢可用時間段
    @GetMapping("/reservation/getAvailableTimeSlots")
    public ReservationRes getAvailableTimeSlots (
            @RequestParam("reservationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            @RequestParam int reservationPeople) {

        return reservationService.findAvailableTimeSlots(reservationDate, reservationPeople);
    }
   
    // 2. 儲存訂位
    @PostMapping (value = "/reservation/saveReservation")
    public ReservationRes saveReservation (@RequestBody ReservationReq reservationReq) {
        return reservationService.saveReservation(reservationReq);
    }

    // 3. 根據顧客電話號碼查詢訂位
    @GetMapping (value = "/reservation/findReservationsByPhoneNumber")
    public ReservationRes findReservationsByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return reservationService.findReservationsByPhoneNumber(phoneNumber);
    }

    // 4. 查詢當天的所有訂位
    @GetMapping (value = "/reservation/findReservationsByDate")
    public ReservationRes findReservationsByDate (
        @RequestParam ("date") @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.findReservationsByDate(date);
    }

    // 5. 取消訂位
    @DeleteMapping (value = "/reservation/cancelReservation")
    public ReservationRes cancelReservation (@RequestParam ("reservationId") int reservationId) {
        return reservationService.cancelReservation(reservationId);
    }
    
    // 6. 手動觸發自動更新桌位狀態（供測試用途）
    @PostMapping (value = "/reservation/autoUpdateTableStatus")
    public ReservationRes manualUpdateTableStatus () {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        return reservationService.autoUpdateTableStatus(currentDate, currentTime);
    }
    
    // 7. 手動報到更新桌位狀態
    @PostMapping (value = "/reservation/manualCheckIn")
    public ReservationRes manualCheckIn (@RequestParam ("tableNumber") String tableNumber, int reservationId) {
        return reservationService.manualCheckIn(tableNumber, reservationId);
    }
}