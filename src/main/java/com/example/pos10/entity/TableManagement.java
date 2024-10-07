package com.example.POS.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table (name = "table_management")
public class TableManagement {

    @Id
    @NotBlank (message = "Table number cannot be null or empty !!!")
    @Column (name = "table_number", nullable = false)
    private String tableNumber;

    @NotNull (message = "Table capacity cannot be null !!!")
    @Column (name = "table_capacity", nullable = false)
    private int tableCapacity;

    public enum TableStatus {
        AVAILABLE, RESERVED, ACTIVE
    }

    @Enumerated (EnumType.STRING)
    @NotNull (message = "Table status cannot be null !!!")
    @Column (name = "table_status", nullable = false)
    private TableStatus tableStatus;
    
//    @ManyToOne
//    @JoinColumn (name = "reservation_id")
//    private Reservation reservation;
//    
    public TableManagement () {
		super ();
	}

	public TableManagement (String tableNumber, int tableCapacity, TableStatus tableStatus, Reservation reservation) {
		super ();
		this.tableNumber = tableNumber;
		this.tableCapacity = tableCapacity;
		this.tableStatus = tableStatus;
//		this.reservation = reservation;
	}

	public String getTableNumber () {
        return tableNumber;
    }

    public void setTableNumber (String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getTableCapacity () {
        return tableCapacity;
    }

    public void setTableCapacity (int tableCapacity) {
        this.tableCapacity = tableCapacity;
    }

    public TableStatus getTableStatus () {
        return tableStatus;
    }

    public void setTableStatus (TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }

//	public Reservation getReservation () {
//		return reservation;
//	}
//
//	public void setReservation (Reservation reservation) {
//		this.reservation = reservation;
//	}
}