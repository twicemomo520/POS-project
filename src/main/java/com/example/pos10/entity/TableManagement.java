package com.example.pos10.entity;

import java.util.List;
import javax.persistence.*;
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

    public enum TableStatus {
        可使用, 訂位中, 用餐中
    }

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Table status cannot be null !!!")
    @Column(name = "table_status", nullable = false)
    private TableStatus tableStatus;

    // 多對多關係，與 Reservation 表關聯
    @ManyToMany(mappedBy = "tables")
    private List<Reservation> reservations; // 這裡指向 Reservation 的多對多關係

    public TableManagement() {
        super();
    }

    public TableManagement(String tableNumber, int tableCapacity, TableStatus tableStatus) {
        this.tableNumber = tableNumber;
        this.tableCapacity = tableCapacity;
        this.tableStatus = tableStatus;
    }

    // Getter 和 Setter
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

    public TableStatus getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}