package com.example.pos10.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.pos10.entity.Staff;

public class AllStaffInfoRes extends BasicRes {

    private List<Staff> staffData = new ArrayList<>();

    public AllStaffInfoRes() {
        super();
    }

    public AllStaffInfoRes(int code, String message, List<Staff> staffData) {
        super(code, message);
        this.staffData = staffData;
    }

    public List<Staff> getStaffData() {
        return staffData;
    }

    public void setStaffData(List<Staff> staffData) {
        this.staffData = staffData;
    }

 
}
