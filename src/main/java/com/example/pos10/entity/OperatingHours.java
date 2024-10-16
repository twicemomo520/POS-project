package com.example.pos10.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "operating_hours")
public class OperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING) // 將枚舉存儲為字串
    private DayOfWeek dayOfWeek; // 星期幾，例如 '星期一', '星期二'

    @Column(name = "opening_time", nullable = false)
    @NotNull(message = "Opening time cannot be null !!!")
    private LocalTime openingTime;  // 開店時間

    @Column(name = "closing_time", nullable = false)
    @NotNull(message = "Closing time cannot be null !!!")
    private LocalTime closingTime;  // 關店時間

    @Column(name = "dining_duration", nullable = false)
    @NotNull(message = "Dining duration cannot be null !!!")
    private int diningDuration; // 用餐時間，以分鐘為單位

    public enum DayOfWeek {
        星期一, 星期二, 星期三, 星期四, 星期五, 星期六, 星期日
    }

    public OperatingHours() {
        super();
    }

    public OperatingHours(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime, int diningDuration) {
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.diningDuration = diningDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public int getDiningDuration() {
        return diningDuration;
    }

    public void setDiningDuration(int diningDuration) {
        this.diningDuration = diningDuration;
    }
}