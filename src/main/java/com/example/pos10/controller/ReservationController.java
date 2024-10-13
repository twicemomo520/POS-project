package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // 1. 查詢並生成可用桌位
    @GetMapping ("/reservation/generateAndFindAvailableTables")
    public ReservationRes generateAndFindAvailableTables (
            @RequestParam ("reservationDate") @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            @RequestParam int diningDuration,
            @RequestParam int reservationPeople) {

        return reservationService.generateAndFindAvailableTables(reservationDate, diningDuration, reservationPeople);
    }
    
    // 2. 計算可用的開始時間段
    @GetMapping ("/reservation/calculateAvailableStartTimes")
    public List <LocalTime> calculateAvailableStartTimes (
            @RequestParam ("openingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime openingTime,
            @RequestParam ("closingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime closingTime,
            @RequestParam int diningDuration) {

        // 呼叫服務層計算可用開始時間段
        return reservationService.calculateAvailableStartTimes(openingTime, closingTime, diningDuration);
    }
    
    // 3. 儲存訂位
    @PostMapping (value = "/reservation/saveReservation")
    public ReservationRes saveReservation (@RequestBody ReservationReq reservationReq) {
    	 System.out.println("接收到的訂位請求：" + reservationReq.toString());
        return reservationService.saveReservation(reservationReq);
    }

    // 4. 根據顧客電話號碼查詢訂位
    @GetMapping (value = "/reservation/findReservationsByPhoneNumber")
    public ReservationRes findReservationsByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return reservationService.findReservationsByPhoneNumber(phoneNumber);
    }

    // 5. 查詢當天的所有訂位
    @GetMapping (value = "/reservation/findReservationsByDate")
    public ReservationRes findReservationsByDate (
        @RequestParam ("date") @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.findReservationsByDate(date);
    }

    // 6. 取消訂位
    @DeleteMapping (value = "/reservation/cancelReservation")
    public ReservationRes cancelReservation (@RequestParam ("reservationId") int reservationId) {
        return reservationService.cancelReservation(reservationId);
    }
    
    // 7. 手動觸發自動更新桌位狀態（供測試用途）
    @PostMapping (value = "/reservation/autoUpdateTableStatus")
    public ReservationRes manualUpdateTableStatus () {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        return reservationService.autoUpdateTableStatus(currentDate, currentTime);
    }
    
    // 8. 手動報到更新桌位狀態
    @PostMapping (value = "/reservation/manualCheckIn")
    public ReservationRes manualCheckIn (@RequestParam ("tableNumber") String tableNumber) {
        return reservationService.manualCheckIn(tableNumber);
    }
}