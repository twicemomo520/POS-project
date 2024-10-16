package com.example.pos10.vo;

public class AvailableTimeSlot {

    private String startTime; // 將 LocalTime 改為 String
    private String endTime;   // 將 LocalTime 改為 String
    private boolean available;

    // 無參構造函數
    public AvailableTimeSlot() {
        super();
    }

    // 含 startTime 和 endTime 的構造函數
    public AvailableTimeSlot(String startTime, String endTime) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // 含 startTime, endTime 和 available 的構造函數
    public AvailableTimeSlot(String startTime, String endTime, boolean available) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
    }

    // Getter 和 Setter 方法
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}