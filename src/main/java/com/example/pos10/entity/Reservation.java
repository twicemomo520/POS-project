package com.example.pos10.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "reservation_id")
    private int reservationId;

    @NotBlank (message = "Customer name cannot be null or empty !!!")
    @Column (name = "customer_name", nullable = false)
    private String customerName;

    @NotBlank (message = "Customer phone number cannot be null or empty !!!")
    @Column (name = "customer_phone_number", nullable = false)
    private String customerPhoneNumber;

    @NotBlank (message = "Customer email cannot be null or empty !!!!")
    @Email (message = "Invalid email format!")
    @Column (name = "customer_email", nullable = false)
    private String customerEmail;

    @Enumerated (EnumType.STRING)
    @Column (name = "customer_gender", nullable = false)
    private Gender customerGender;

    @NotNull (message = "Reservation people cannot be null !!!")
    @Column (name = "reservation_people", nullable = false)
    private int reservationPeople;

    // 預約日期必須是當前或未來的日期，不允許為 null
    @DateTimeFormat (iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent (message = "Reservation date must be in the present or future !!!")
    @NotNull (message = "Reservation date cannot be null !!!")
    @Column (name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    // 預約開始時間，不允許為 null
    @NotNull (message = "Start time cannot be null !!!")
    @Column (name = "reservation_starttime", nullable = false)
    private LocalTime reservationStartTime;
    
    // 預約結束時間
    @Column (name = "reservation_endtime", nullable = false)
    private LocalTime reservationEndTime;

    @OneToMany(mappedBy = "reservation")
    @JsonManagedReference("reservation-reservationTables") // 使用唯一名稱
    private List <ReservationAndTable> reservationTables; // 關聯到中間表

    public enum Gender {
        先生, 小姐
    }

    public Reservation () {
        super();
    }

    public Reservation(int reservationId, String customerName, String customerPhoneNumber, String customerEmail,Gender customerGender,
    		int reservationPeople, LocalDate reservationDate, LocalTime reservationStartTime, LocalTime reservationEndTime,
    		List <ReservationAndTable> reservationTables) {
		super();
		this.reservationId = reservationId;
		this.customerName = customerName;
		this.customerPhoneNumber = customerPhoneNumber;
		this.customerEmail = customerEmail;
		this.customerGender = customerGender;
		this.reservationPeople = reservationPeople;
		this.reservationDate = reservationDate;
		this.reservationStartTime = reservationStartTime;
		this.reservationEndTime = reservationEndTime;
		this.reservationTables = reservationTables;
	}

	public int getReservationId () {
        return reservationId;
    }

    public void setReservationId (int reservationId) {
        this.reservationId = reservationId;
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

    public int getReservationPeople () {
        return reservationPeople;
    }

    public void setReservationPeople (int reservationPeople) {
        this.reservationPeople = reservationPeople;
    }

    public LocalDate getReservationDate () {
        return reservationDate;
    }

    public void setReservationDate (LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationStartTime () {
        return reservationStartTime;
    }

    public void setReservationStartTime (LocalTime reservationStartTime) {
        this.reservationStartTime = reservationStartTime;
    }

    public LocalTime getReservationEndingTime () {
        return reservationEndTime;
    }

    public void setReservationEndingTime (LocalTime reservationEndingTime) {
        this.reservationEndTime = reservationEndingTime;
    }

	public List<ReservationAndTable> getReservationTables() {
		return reservationTables;
	}

	public void setReservationTables(List<ReservationAndTable> reservationTables) {
		this.reservationTables = reservationTables;
	}
}