package com.example.pos10.vo;

import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.ReservationManagement;

public class ReservationManagementRes {

    private int code;
    private String message;
    private List <AvailableTimeSlot> availableTimeSlots; // 用於返回與桌位狀態和日期結合的可用訂位列表
    private List <LocalTime> availableStartTimes; // 用於顯示營業時間和用餐時間計算的可用時間段
    private List <ReservationManagement> reservationRecords; // 用於返回生成的 ReservationManagement 記錄

    public ReservationManagementRes () {
        super ();
    }

    public ReservationManagementRes (int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 第一個構造方法，用於可用時間段和 LocalTime 列表
    public ReservationManagementRes (int code, String message, List<AvailableTimeSlot> availableTimeSlots, List<LocalTime> availableStartTimes) {
        this.code = code;
        this.message = message;
        this.availableTimeSlots = availableTimeSlots;
        this.availableStartTimes = availableStartTimes;
    }

    // 第二個構造方法，使用不同的參數來避免擦除後的重複
    public ReservationManagementRes (int code, String message, List<AvailableTimeSlot> availableTimeSlots, 
    		List <ReservationManagement> reservationRecords, boolean isReservation) {
        this.code = code;
        this.message = message;
        this.availableTimeSlots = availableTimeSlots;
        this.reservationRecords = reservationRecords;
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

	public List <ReservationManagement> getReservationRecords () {
		return reservationRecords;
	}

	public void setReservationRecords (List <ReservationManagement> reservationRecords) {
		this.reservationRecords = reservationRecords;
	}
}