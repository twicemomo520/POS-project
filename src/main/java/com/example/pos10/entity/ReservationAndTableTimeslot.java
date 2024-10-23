package com.example.pos10.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_and_table_timeslot")
public class ReservationAndTableTimeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "table_number")
    private String tableNumber;

    @Column(name = "operating_hour_id")
    private int operatingHourId;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "table_status")
    private TableStatus tableStatus;

    public enum TableStatus {
        可使用, 已訂位, 用餐中
    }

    public ReservationAndTableTimeslot() {
		super();
	}

	public ReservationAndTableTimeslot(int id, Integer reservationId, String tableNumber, int operatingHourId, LocalDate reservationDate,
    		TableStatus tableStatus) {
		super();
		this.id = id;
		this.reservationId = reservationId;
		this.tableNumber = tableNumber;
		this.operatingHourId = operatingHourId;
		this.reservationDate = reservationDate;
		this.tableStatus = tableStatus;
	}



	// Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getOperatingHourId() {
        return operatingHourId;
    }

    public void setOperatingHourId(int operatingHourId) {
        this.operatingHourId = operatingHourId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public TableStatus getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }
}