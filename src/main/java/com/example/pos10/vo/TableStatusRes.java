package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.ReservationAndTable;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.entity.TableManagement.TableStatus;

public class TableStatusRes {

    private String tableNumber; // 桌位號碼
    private int tableCapacity;  // 桌位容量
    private TableManagement.TableStatus tableStatus;  // 桌位狀態
    private List <ReservationAndTable> reservationTables;  // 與桌位相關的預約列表

    public TableStatusRes () {
		super ();
	}
    
    public TableStatusRes (String tableNumber, int tableCapacity, TableManagement.TableStatus tableStatus, List<ReservationAndTable> reservationTables) {
        this.tableNumber = tableNumber;
        this.tableCapacity = tableCapacity;
        this.tableStatus = tableStatus;
        this.reservationTables = reservationTables;
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

	public TableManagement.TableStatus getTableStatus () {
		return tableStatus;
	}

	public void setTableStatus (TableManagement.TableStatus tableStatus) {
		this.tableStatus = tableStatus;
	}

	public List <ReservationAndTable> getReservationTables() {
		return reservationTables;
	}

	public void setReservationTables (List <ReservationAndTable> reservationTables) {
		this.reservationTables = reservationTables;
	}
}
