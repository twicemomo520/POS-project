package com.example.pos10.service.ifs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.pos10.vo.ReservationManagementRes;

public interface ReservationManagementService {

    // 1. 查詢可用桌位並儲存
	public ReservationManagementRes generateAndFindAvailableTables(LocalDate reservationDate, int diningDuration);
    
    // 2. 計算可用的開始時間段
    public List <LocalTime> calculateAvailableStartTimes (LocalTime openingTime, LocalTime closingTime, int diningDuration);

}