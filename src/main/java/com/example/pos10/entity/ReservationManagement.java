package com.example.POS.project.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;


@Entity
@Table (name = "reservation_management")
public class ReservationManagement {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "index_id")
    private int indexId;

    // 多對一關係，連接到 TableManagement 表中的 table_number，不允許為 null
    @ManyToOne
    @JoinColumn (name = "table_number", nullable = false)
    private TableManagement tableManagement;

    // 預約日期必須是當前或未來的日期，不允許為 null
    @FutureOrPresent (message = "Reservation date must be in the present or future !!!")
    @NotNull (message = "Reservation date cannot be null !!!")
    @Column (name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    // 預約開始時間，不允許為 null
    @NotNull (message = "Start time cannot be null !!!")
    @Column (name = "reservation_starttime", nullable = false)
    private LocalTime reservationStarttime;
    
    // 預約結束時間，不允許為 null
    @NotNull(message = "Ending time cannot be null !!!")
    @Column(name = "reservation_endingtime", nullable = false)
    private LocalTime reservationEndingTime;

	public ReservationManagement () {
		super ();
	}

	public ReservationManagement(int indexId, TableManagement tableManagement, LocalDate reservationDate, LocalTime reservationStarttime) {
		super ();
		this.indexId = indexId;
		this.tableManagement = tableManagement;
		this.reservationDate = reservationDate;
		this.reservationStarttime = reservationStarttime;
		this.reservationEndingTime = reservationEndingTime;
	}

	public int getIndexId () {
		return indexId;
	}

	public void setIndexId (int indexId) {
		this.indexId = indexId;
	}

	public TableManagement getTableManagement () {
		return tableManagement;
	}

	public void setTableManagement (TableManagement tableManagement) {
		this.tableManagement = tableManagement;
	}

	public LocalDate getReservationDate () {
		return reservationDate;
	}

	public void setReservationDate (LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}

	public LocalTime getReservationStarttime () {
		return reservationStarttime;
	}

	public void setReservationStarttime (LocalTime reservationStarttime) {
		this.reservationStarttime = reservationStarttime;
	}
	
	public LocalTime getReservationEndingTime () {
		return reservationEndingTime;
	}

	public void setReservationEndingTime (LocalTime reservationEndingTime) {
		this.reservationEndingTime = reservationEndingTime;
	}
}