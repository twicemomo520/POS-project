package com.example.POS.project.vo;

import java.time.LocalTime;

public class AvailableTimeSlot {

    private LocalTime startTime;
    
    private LocalTime endTime;
    
    private boolean available;

	public AvailableTimeSlot () {
		super ();
	}

	public AvailableTimeSlot (LocalTime startTime, LocalTime endTime, boolean available) {
		super ();
		this.startTime = startTime;
		this.endTime = endTime;
		this.available = available;
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

	public boolean isAvailable () {
		return available;
	}

	public void setAvailable (boolean available) {
		this.available = available;
	}
}