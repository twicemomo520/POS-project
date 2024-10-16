package com.example.pos10.vo;

import com.example.pos10.entity.TableManagement;

public class TableManagementReq extends TableManagement {

    public TableManagementReq () {
        super ();
    }

	public TableManagementReq(String tableNumber, int tableCapacity, TableStatus tableStatus) {
		super (tableNumber, tableCapacity, tableStatus);
	}
}