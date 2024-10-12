package com.example.pos10.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table (name = "waitlist")
public class Waitlist {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "waitlist_id")
    private int waitlistId;

    @NotBlank(message = "Customer name cannot be null or empty !!!")
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotBlank(message = "Customer phone number cannot be null or empty !!!")
    @Column(name = "customer_phone_number", nullable = false)
    private String customerPhoneNumber;
    
    @NotBlank(message = "Customer email cannot be null or empty !!!!")
    @Email(message = "Invalid email format!")
    @Column(name = "customer_email", nullable = false)
    private String customerEmail;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_gender", nullable = false)
    private Gender customerGender;

    @NotNull(message = "Party size cannot be null !!!")
    @Column(name = "waitlist_people", nullable = false)
    private int waitListPeople;
    
    // 候位日期必須是當前或未來的日期，不允許為 null
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent(message = "Waiting date must be in the present or future !!!")
    @NotNull(message = "Waiting date cannot be null !!!")
    @Column(name = "waiting_date", nullable = false)
    private LocalDate waitingDate;

    // 候位登記時間
    @NotNull(message = "Wait time cannot be null !!!")
    @Column(name = "wait_time", nullable = false)
    private LocalTime waitTime; 

    @NotNull(message = "Waitlist order cannot be null !!!")
    @Column(name = "waitlist_order", nullable = false)
    private int waitlistOrder; // 用於紀錄候位順序

    public enum Gender {
        先生, 小姐
    }
    
    public Waitlist () {
        super();
    }

	public Waitlist (int waitlistId, String customerName, String customerPhoneNumber, String customerEmail,Gender customerGender,
			int waitListPeople, LocalDate waitingDate, LocalTime waitTime, int waitlistOrder) {
		this.waitlistId = waitlistId;
		this.customerName = customerName;
		this.customerPhoneNumber = customerPhoneNumber;
		this.customerEmail = customerEmail;
		this.customerGender = customerGender;
		this.waitListPeople = waitListPeople;
		this.waitingDate = waitingDate;
		this.waitTime = waitTime;
		this.waitlistOrder = waitlistOrder;
	}

	public int getWaitlistId () {
		return waitlistId;
	}

	public void setWaitlistId (int waitlistId) {
		this.waitlistId = waitlistId;
	}

	public String getCustomerName () {
		return customerName;
	}

	public void setCustomerName (String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhoneNumber () {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber (String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public String getCustomerEmail () {
		return customerEmail;
	}

	public void setCustomerEmail (String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Gender getCustomerGender () {
		return customerGender;
	}

	public void setCustomerGender (Gender customerGender) {
		this.customerGender = customerGender;
	}

	public int getWaitListPeople () {
		return waitListPeople;
	}

	public void setWaitListPeople (int waitListPeople) {
		this.waitListPeople = waitListPeople;
	}

	public LocalDate getWaitingDate () {
		return waitingDate;
	}

	public void setWaitingDate (LocalDate waitingDate) {
		this.waitingDate = waitingDate;
	}

	public LocalTime getWaitTime () {
		return waitTime;
	}

	public void setWaitTime (LocalTime waitTime) {
		this.waitTime = waitTime;
	}

	public int getWaitlistOrder () {
		return waitlistOrder;
	}

	public void setWaitlistOrder (int waitlistOrder) {
		this.waitlistOrder = waitlistOrder;
	}
}