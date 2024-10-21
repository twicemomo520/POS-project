package com.example.pos10.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class ReservationAndTableId implements Serializable {
    private static final long serialVersionUID = 1L;

    private int reservationId;
    private String tableNumber;

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservationAndTableId other = (ReservationAndTableId) obj;
		return reservationId == other.reservationId && Objects.equals(tableNumber, other.tableNumber);
	}

    @Override
	public int hashCode() {
		return Objects.hash(reservationId, tableNumber);
	}

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
}