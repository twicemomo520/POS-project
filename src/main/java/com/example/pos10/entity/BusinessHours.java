package com.example.POS.project.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table (name = "business_hours")
public class BusinessHours {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    // 暫時固定 storeId 為 1，未來可擴展到多店家
    @Column (name = "store_id", nullable = false)
    @NotNull (message = "Store ID cannot be null !!!")
    private int storeId;

    @Column (name = "day_of_week", nullable = false)
    @NotBlank (message = "Day of the week cannot be null or empty !!!")
    private String dayOfWeek;  // 星期幾，例如 'Monday', 'Tuesday'

    @Column (name = "opening_time", nullable = false)
    @NotNull (message = "Opening time cannot be null !!!")
    private LocalTime openingTime;  // 開店時間

    @Column (name = "closing_time", nullable = false)
    @NotNull (message = "Closing time cannot be null !!!")
    private LocalTime closingTime;  // 關店時間
    
    // ManyToOne 關聯到 DiningDuration
    @ManyToOne
    @JoinColumn(name = "dining_duration_id", referencedColumnName = "id") // dining_duration_id 是外鍵
    private DiningDuration diningDuration;

    public BusinessHours () {
    	this.storeId = 1;  // 默認為 1
    }

    public BusinessHours (String dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
    	this.storeId = 1;  // 默認為 1
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getStoreId () {
        return storeId;
    }

    public void setStoreId (int storeId) {
        this.storeId = storeId;
    }

    public String getDayOfWeek () {
        return dayOfWeek;
    }

    public void setDayOfWeek (String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime () {
        return openingTime;
    }

    public void setOpeningTime (LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime () {
        return closingTime;
    }

    public void setClosingTime (LocalTime closingTime) {
        this.closingTime = closingTime;
    }

	public DiningDuration getDiningDuration () {
		return diningDuration;
	}

	public void setDiningDuration (DiningDuration diningDuration) {
		this.diningDuration = diningDuration;
	}
}