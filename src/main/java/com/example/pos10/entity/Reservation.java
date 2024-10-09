package com.example.pos10.entity;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

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

    // 與 ReservationManagement 之間的多對一關係
    @ManyToOne
    @JoinColumn (name = "reservation_management_id", nullable = false) // 外鍵，連接到 ReservationManagement
    private ReservationManagement reservationManagement;
    
    // 與 TableManagement 之間的多對多關係
    @ManyToMany
    @JoinTable (
        name = "reservation_table", // 中間表名
        joinColumns = @JoinColumn (name = "reservation_id"), // Reservation 的外鍵
        inverseJoinColumns = @JoinColumn (name = "table_number") // TableManagement 的外鍵，改為 table_number
    )
    private List<TableManagement> tables; // 用來儲存分配的桌位


    public enum Gender {
        男性, 女性
    }

	public Reservation () {
		super ();
	}


	public Reservation (int reservationId, String customerName, String customerPhoneNumber, String customerEmail,Gender customerGender, 
			int reservationPeople, ReservationManagement reservationManagement, List<TableManagement> tables) {
		super ();
		this.reservationId = reservationId;
		this.customerName = customerName;
		this.customerPhoneNumber = customerPhoneNumber;
		this.customerEmail = customerEmail;
		this.customerGender = customerGender;
		this.reservationPeople = reservationPeople;
		this.reservationManagement = reservationManagement;
		this.tables = tables;
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

	public ReservationManagement getReservationManagement () {
		return reservationManagement;
	}

	public void setReservationManagement (ReservationManagement reservationManagement) {
		this.reservationManagement = reservationManagement;
	}

	public List <TableManagement> getTables () {
		return tables;
	}

	public void setTables (List <TableManagement> tables) {
		this.tables = tables;
	}
}