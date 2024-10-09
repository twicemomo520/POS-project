package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.BusinessHours;
import com.example.pos10.entity.ReservationManagement;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.BusinessHoursDao;
import com.example.pos10.repository.ReservationManagementDao;
import com.example.pos10.service.ifs.ReservationManagementService;
import com.example.pos10.vo.AvailableTimeSlot;
import com.example.pos10.vo.ReservationManagementRes;


@Service
public class ReservationManagementServiceImpl implements ReservationManagementService {

    @Autowired
    private ReservationManagementDao reservationManagementDao;

    @Autowired
    private BusinessHoursDao businessHoursDao;
    
    // 1.查詢可用的桌位並儲存
    @Override
    public ReservationManagementRes generateAndFindAvailableTables(LocalDate reservationDate, int diningDuration) {
        // 查詢當天的營業時段
        String dayOfWeek = reservationDate.getDayOfWeek().toString();
        List<BusinessHours> businessHoursList = businessHoursDao.findBusinessHoursByDayAndStore(1, dayOfWeek);

        if (businessHoursList.isEmpty()) {
            return new ReservationManagementRes(ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getCode(),
                                                ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getMessage());
        }

        List<AvailableTimeSlot> availableTimeSlots = new ArrayList<>();
        List<ReservationManagement> generatedRecords = new ArrayList<>();

        // 遍歷每個營業時段
        for (BusinessHours businessHours : businessHoursList) {
            LocalTime openingTime = businessHours.getOpeningTime();
            LocalTime closingTime = businessHours.getClosingTime();
            LocalTime currentTime = openingTime;

            while (currentTime.isBefore(closingTime)) {
                LocalTime slotEndTime = currentTime.plusMinutes(diningDuration);

                // 確保結束時間不超過營業結束時間
                if (slotEndTime.isAfter(closingTime)) {
                    break;
                }

                // 查詢這個時間段內的可用桌位
                List<TableManagement> availableTables = reservationManagementDao.findAvailableTables(reservationDate, currentTime, slotEndTime);

                // 如果有可用桌位，創建並儲存 ReservationManagement 記錄
                if (!availableTables.isEmpty()) {
                    ReservationManagement reservationManagement = new ReservationManagement();
                    reservationManagement.setReservationDate(reservationDate);
                    reservationManagement.setReservationStarttime(currentTime);
                    reservationManagement.setReservationEndingTime(slotEndTime);
                    reservationManagementDao.save(reservationManagement);
                    generatedRecords.add(reservationManagement);
                }

                // 設定可用時間段的狀態
                AvailableTimeSlot timeSlot = new AvailableTimeSlot();
                timeSlot.setStartTime(currentTime);
                timeSlot.setEndTime(slotEndTime);
                timeSlot.setAvailable(!availableTables.isEmpty());
                availableTimeSlots.add(timeSlot);

                // 每次增加 30 分鐘的間隔
                currentTime = currentTime.plusMinutes(30);
            }
        }

        if (availableTimeSlots.isEmpty()) {
            return new ReservationManagementRes(ResMessage.NO_RESERVED_TIME_SLOTS.getCode(),
                                                ResMessage.NO_RESERVED_TIME_SLOTS.getMessage());
        }

     // 返回生成的 ReservationManagement 和可用時間段
        return new ReservationManagementRes(ResMessage.SUCCESS.getCode(),
                                            ResMessage.SUCCESS.getMessage(), availableTimeSlots, generatedRecords, true);
    }


    // 2. 計算可用的開始時間段
    @Override
    public List <LocalTime> calculateAvailableStartTimes (LocalTime openingTime, LocalTime closingTime, int diningDuration) {
        if (openingTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("營業開始時間不能晚於結束時間！");
        }

        if (diningDuration > ChronoUnit.MINUTES.between(openingTime, closingTime)) {
            throw new IllegalArgumentException("用餐時長超過了營業時間！");
        }

        List <LocalTime> availableStartTimes = new ArrayList<>();
        LocalTime currentTime = openingTime;

        // 根據營業時間和用餐時間計算可用時間段
        while (currentTime.isBefore(closingTime)) {
            LocalTime slotEndTime = currentTime.plusMinutes(diningDuration);

            // 確保結束時間不超過營業時間
            if (slotEndTime.isAfter(closingTime)) {
                break;
            }

            availableStartTimes.add(currentTime);
            currentTime = currentTime.plusMinutes(diningDuration);
        }

        return availableStartTimes;
    }
}