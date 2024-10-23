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

    // 使用 @ManyToOne 來表示與 Reservation 的關聯
    @ManyToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id")
    private Reservation reservation;  // 改為引用 Reservation 實體，而不是手動存儲 reservationId

    // 與 TableManagement 表的關聯
    @ManyToOne
    @JoinColumn(name = "table_number", referencedColumnName = "table_number")
    private TableManagement tableManagement; // 引用 TableManagement 實體

    // 與 OperatingHours 表的關聯
    @ManyToOne
    @JoinColumn(name = "operating_hour_id", referencedColumnName = "id")
    private OperatingHours operatingHours; // 引用 OperatingHours 實體

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "table_status")
    private TableStatus tableStatus;

    public enum TableStatus {
        可使用, 已訂位, 用餐中
    }

    // Constructor
    public ReservationAndTableTimeslot() {
        super();
    }

    public ReservationAndTableTimeslot(int id, Reservation reservation, TableManagement tableManagement, 
                                       OperatingHours operatingHours, LocalDate reservationDate, TableStatus tableStatus) {
        this.id = id;
        this.reservation = reservation;
        this.tableManagement = tableManagement;
        this.operatingHours = operatingHours;
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public TableManagement getTableManagement() {
        return tableManagement;
    }

    public void setTableManagement(TableManagement tableManagement) {
        this.tableManagement = tableManagement;
    }

    public OperatingHours getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(OperatingHours operatingHours) {
        this.operatingHours = operatingHours;
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