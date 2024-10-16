package com.example.pos10.vo;

import java.time.LocalTime;
import java.util.List;
import com.example.pos10.entity.TableManagement;

public class TableManagementRes {
    
    private int code;
    private String message;
    
    // 現有的桌位列表
    private List<TableManagement> tables; 
    
    // 新增的可用時間段列表
    private List<TimeSlotWithTableStatusRes> availableTimeSlots; // TimeSlotWithTableStatus 是你需要定義的類

    public TableManagementRes() {
        super();
    }

    public TableManagementRes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public TableManagementRes(int code, String message, List<TableManagement> tables, List<TimeSlotWithTableStatusRes> availableTimeSlots) {
        super();
        this.code = code;
        this.message = message;
        this.tables = tables;
        this.availableTimeSlots = availableTimeSlots; // 初始化可用時間段
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

    public List<TableManagement> getTables() {
        return tables;
    }

    public void setTables(List<TableManagement> tables) {
        this.tables = tables;
    }

    public List<TimeSlotWithTableStatusRes> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<TimeSlotWithTableStatusRes> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
}