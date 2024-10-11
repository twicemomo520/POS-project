package com.example.pos10.vo;

import java.time.LocalTime;
import java.util.List;

public class ReservationRes {

    private int code;
    private String message;
    private List<AvailableTimeSlot> availableTimeSlots; // 用於返回與桌位狀態和日期結合的可用訂位列表
    private List<LocalTime> availableStartTimes; // 用於顯示營業時間和用餐時間計算的可用時間段
    private List<ReservationReq> reservations; // 用於返回 ReservationReq 列表

    public ReservationRes() {
        super();
    }

    public ReservationRes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 用於可用時間段和 LocalTime 列表的構造方法
    public ReservationRes(int code, String message, List<AvailableTimeSlot> availableTimeSlots, List<LocalTime> availableStartTimes) {
        this.code = code;
        this.message = message;
        this.availableTimeSlots = availableTimeSlots;
        this.availableStartTimes = availableStartTimes;
    }

    // 用於 ReservationReq 列表的構造方法
    public ReservationRes(int code, String message, List<ReservationReq> reservations) {
        this.code = code;
        this.message = message;
        this.reservations = reservations;
    }

    // Getter 和 Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AvailableTimeSlot> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<AvailableTimeSlot> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public List<LocalTime> getAvailableStartTimes() {
        return availableStartTimes;
    }

    public void setAvailableStartTimes(List<LocalTime> availableStartTimes) {
        this.availableStartTimes = availableStartTimes;
    }

    public List<ReservationReq> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationReq> reservations) {
        this.reservations = reservations;
    }
}