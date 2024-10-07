package com.example.pos10.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "dining_duration")
public class DiningDuration {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "duration_minutes", nullable = false)
    @Min (value = 30, message = "Dining duration must be at least 30 minutes !!!")
    @NotNull (message = "Dining duration cannot be null !!!")
    private int durationMinutes;  // 用餐時長，以分鐘為單位

    // OneToMany 關聯到 BusinessHours
    @OneToMany(mappedBy = "diningDuration", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BusinessHours> businessHoursList = new ArrayList<>(); // 預設初始化
    
    public DiningDuration () {
    }

    public DiningDuration (int durationMinutes, List <BusinessHours> businessHours) {
        this.durationMinutes = durationMinutes;
        this.businessHoursList = businessHours;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getDurationMinutes () {
        return durationMinutes;
    }

    public void setDurationMinutes (int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

	public List <BusinessHours> getBusinessHoursList() {
		return businessHoursList;
	}

	public void setBusinessHoursList (List <BusinessHours> businessHoursList) {
		this.businessHoursList = businessHoursList;
	}
}