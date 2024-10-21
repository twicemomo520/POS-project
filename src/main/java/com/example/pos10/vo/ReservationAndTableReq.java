package com.example.pos10.vo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.pos10.entity.ReservationAndTable;
import com.example.pos10.entity.ReservationAndTableId;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;

public class ReservationAndTableReq extends ReservationAndTable {

    private int reservationPeople;  // 添加預約人數屬性
    private List <TableStatus> statuses;  // 用於多狀態查詢的屬性

    public ReservationAndTableReq () {
        super ();
    }

    public ReservationAndTableReq (ReservationAndTableId reservationTableId, Reservation reservation,
                                  TableManagement table, LocalDate reservationDate, LocalTime reservationStartTime,
                                  LocalTime reservationEndTime, TableStatus tableStatus) {
        super (reservationTableId, reservation, table, reservationDate, reservationStartTime, reservationEndTime, tableStatus);
    }

    public int getReservationPeople () {
        return reservationPeople;
    }

    public void setReservationPeople (int reservationPeople) {
        this.reservationPeople = reservationPeople;
    }

    public List <TableStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses (List <TableStatus> statuses) {
        this.statuses = statuses;
    }
}