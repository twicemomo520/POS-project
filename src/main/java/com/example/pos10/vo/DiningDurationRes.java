package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.DiningDuration;

public class DiningDurationRes {

    private int code;
    private String message;
    private int diningDurationId; // 新創建或更新的用餐時間 ID
    private List<DiningDuration> diningDurations; // 用餐時間列表

    // 構造方法、Getter 和 Setter
    public DiningDurationRes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public DiningDurationRes(int code, String message, List<DiningDuration> diningDurations) {
        this.code = code;
        this.message = message;
        this.diningDurations = diningDurations;
    }

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

    public List<DiningDuration> getDiningDurations() {
        return diningDurations;
    }

    public void setDiningDurations(List<DiningDuration> diningDurations) {
        this.diningDurations = diningDurations;
    }
}