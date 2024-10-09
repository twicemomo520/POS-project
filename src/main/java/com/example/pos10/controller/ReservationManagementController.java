package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.ReservationManagement;
import com.example.pos10.service.ifs.ReservationManagementService;
import com.example.pos10.vo.ReservationManagementRes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ReservationManagementController {

    @Autowired
    private ReservationManagementService reservationManagementService;

    // 1. 查詢並生成可用桌位
    @GetMapping ("/reservationManagement/generateAndFindAvailableTables")
    public ReservationManagementRes generateAndFindAvailableTables(
            @RequestParam("reservationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            @RequestParam int diningDuration) {

        return reservationManagementService.generateAndFindAvailableTables(reservationDate, diningDuration);
    }
    
    // 2. 計算可用的開始時間段
    @GetMapping("/reservationManagement/calculateAvailableStartTimes")
    public List <LocalTime> calculateAvailableStartTimes (
            @RequestParam ("openingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime openingTime,
            @RequestParam ("closingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime closingTime,
            @RequestParam int diningDuration) {

        // 呼叫服務層計算可用開始時間段
        return reservationManagementService.calculateAvailableStartTimes(openingTime, closingTime, diningDuration);
    }
}