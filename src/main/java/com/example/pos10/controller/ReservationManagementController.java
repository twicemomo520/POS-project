package com.example.POS.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.POS.project.service.ifs.ReservationManagementService;
import com.example.POS.project.vo.ReservationManagementRes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class ReservationManagementController {

    @Autowired
    private ReservationManagementService reservationManagementService;

    // 查詢可用桌位
    @GetMapping("/reservationManagement/findAvailableTables")
	public ReservationManagementRes findAvailableTables (
			@RequestParam ("reservationDate") @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
			@RequestParam ("startTime") @DateTimeFormat( iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
			@RequestParam int diningDuration) {

		return reservationManagementService.findAvailableTables(reservationDate, startTime, diningDuration);
	}
    
    // 計算可用的開始時間段
    @GetMapping("/reservationManagement/calculateAvailableStartTimes")
    public List <LocalTime> calculateAvailableStartTimes (
            @RequestParam ("openingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime openingTime,
            @RequestParam ("closingTime") @DateTimeFormat (iso = DateTimeFormat.ISO.TIME) LocalTime closingTime,
            @RequestParam int diningDuration) {

        // 呼叫服務層計算可用開始時間段
        return reservationManagementService.calculateAvailableStartTimes(openingTime, closingTime, diningDuration);
    }
}