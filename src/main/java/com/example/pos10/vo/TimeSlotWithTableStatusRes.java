package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.TableManagement;

public class TimeSlotWithTableStatusRes {
	private String timeSlot;
    private List<TableManagement> tableStatuses;

    public TimeSlotWithTableStatusRes(String timeSlot, List<TableManagement> tableStatuses) {
		super();
		this.timeSlot = timeSlot;
		this.tableStatuses = tableStatuses;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public List<TableManagement> getTableStatuses() {
		return tableStatuses;
	}

	public void setTableStatuses(List<TableManagement> tableStatuses) {
		this.tableStatuses = tableStatuses;
	}
}