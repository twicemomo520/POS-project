package com.example.pos10.vo;

import java.util.List;

import com.example.pos10.entity.ReservationAndTable;
import com.example.pos10.entity.TableManagement;

public class TableManagementReq extends TableManagement {

    public TableManagementReq () {
        super ();
    }

	public TableManagementReq(String tableNumber, int tableCapacity, TableStatus tableStatus, List <ReservationAndTable> reservationTables) {
		super(tableNumber, tableCapacity, tableStatus, reservationTables);
	}
}