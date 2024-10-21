package com.example.pos10.vo;

import java.util.List;

public class ReservationAndTableRes {

    private int code;
    private String message;
    private List <String> tableNumbers; // 添加這個屬性來存儲桌位號碼

    public ReservationAndTableRes () {
        super();
    }

    public ReservationAndTableRes (int code, String message) {
        super();
        this.code = code;
        this.message = message;
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

    public List <String> getTableNumbers () {
        return tableNumbers;
    }

    public void setTableNumbers (List <String> tableNumbers) {
        this.tableNumbers = tableNumbers;
    }
}