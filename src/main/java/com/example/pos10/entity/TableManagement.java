package com.example.pos10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "table_management")
public class TableManagement {

    @Id
    @NotBlank(message = "Table number cannot be null or empty !!!")
    @Column(name = "table_number", nullable = false)
    private String tableNumber;

    @NotNull(message = "Table capacity cannot be null !!!")
    @Column(name = "table_capacity", nullable = false)
    private int tableCapacity;

    public TableManagement() {
        super();
    }

    public TableManagement(String tableNumber, int tableCapacity) {
		super();
		this.tableNumber = tableNumber;
		this.tableCapacity = tableCapacity;
	}
    
    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getTableCapacity() {
        return tableCapacity;
    }

    public void setTableCapacity(int tableCapacity) {
        this.tableCapacity = tableCapacity;
    }
}