package com.example.POS.project.vo;

import java.time.LocalTime;
import java.util.List;

public class ReservationManagementRes {

    private int code;
    private String message;
    private List <AvailableTimeSlot> availableTimeSlots; // 用於返回與桌位狀態和日期結合的可用訂位列表
    private List <LocalTime> availableStartTimes; // 用於顯示營業時間和用餐時間計算的可用時間段

    public ReservationManagementRes () {
        super ();
    }

    public ReservationManagementRes (int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ReservationManagementRes (int code, String message, List <AvailableTimeSlot> availableTimeSlots, //
    		List<LocalTime> availableStartTimes) {
        this.code = code;
        this.message = message;
        this.availableTimeSlots = availableTimeSlots;
        this.availableStartTimes = availableStartTimes;
    }

    public int getCode () {
        return code;
    }

    public void setCode (int code) {
        this.code = code;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public List <AvailableTimeSlot> getAvailableTimeSlots () {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots (List <AvailableTimeSlot> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public List <LocalTime> getAvailableStartTimes () {
        return availableStartTimes;
    }

    public void setAvailableStartTimes (List <LocalTime> availableStartTimes) {
        this.availableStartTimes = availableStartTimes;
    }
}