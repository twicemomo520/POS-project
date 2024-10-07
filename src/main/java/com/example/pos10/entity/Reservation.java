package com.example.POS.project.entity;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;


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

    @JsonFormat (pattern = "HH:mm:ss")
    @NotNull (message = "Reservation time cannot be null !!!")
    @Column (name = "reservation_time", nullable = false)
    private LocalTime reservationTime;

    // 與 ReservationManagement 之間的多對一關係
    @ManyToOne
    @JoinColumn (name = "reservation_management_id", nullable = false) // 外鍵，連接到 ReservationManagement
    private ReservationManagement reservationManagement;

    public enum Gender {
        男性, 女性
    }

	public Reservation () {
		super ();
	}

	public Reservation (int reservationId, String customerName, String customerPhoneNumber, String customerEmail,
			Gender customerGender, int reservationPeople, LocalTime reservationTime, ReservationManagement reservationManagement) {
		super ();
		this.reservationId = reservationId;
		this.customerName = customerName;
		this.customerPhoneNumber = customerPhoneNumber;
		this.customerEmail = customerEmail;
		this.customerGender = customerGender;
		this.reservationPeople = reservationPeople;
		this.reservationTime = reservationTime;
		this.reservationManagement = reservationManagement;
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

	public LocalTime getReservationTime () {
		return reservationTime;
	}

	public void setReservationTime (LocalTime reservationTime) {
		this.reservationTime = reservationTime;
	}

	public ReservationManagement getReservationManagement () {
		return reservationManagement;
	}

	public void setReservationManagement (ReservationManagement reservationManagement) {
		this.reservationManagement = reservationManagement;
	}
}