package com.example.pos10.vo;

import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.TableManagement;

public class TimeSlotWithTableStatusRes {
    private LocalTime startTime;
    private LocalTime endTime;
    private List<TableManagement> allTables; // 此時間段的所有桌位狀態

    public TimeSlotWithTableStatusRes () {
		super();
	}

	public TimeSlotWithTableStatusRes (LocalTime startTime, LocalTime endTime, List<TableManagement> allTables) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.allTables = allTables;
	}

    public LocalTime getStartTime () {
        return startTime;
    }

    public void setStartTime (LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime () {
        return endTime;
    }

    public void setEndTime (LocalTime endTime) {
        this.endTime = endTime;
    }

    public List <TableManagement> getAllTables () {
        return allTables;
    }

    public void setAllTables (List <TableManagement> allTables) {
        this.allTables = allTables;
    }
}