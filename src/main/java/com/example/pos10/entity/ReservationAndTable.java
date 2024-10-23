//package com.example.pos10.entity;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.MapsId;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//
//@Entity
//@Table(name = "reservation_table")
//public class ReservationAndTable {
//	    
//	 	@EmbeddedId
//	    private ReservationAndTableId reservationTableId;  // 使用更具描述性的名稱
//	    
//		@NotNull(message = "ReservationId cannot be null !!!")
//		@ManyToOne
//		@MapsId("reservationId")
//		@JoinColumn(name = "reservation_id")
//		@JsonBackReference("reservation-reservationTables") // 這裡需要和 Reservation 類中的 JsonManagedReference 對應
//		private Reservation reservation;
//
//		@NotNull(message = "TableNumber cannot be null !!!")
//		@ManyToOne
//		@MapsId("tableNumber")
//		@JoinColumn(name = "table_number")
//		@JsonBackReference("table-reservationTables") // 這裡需要和 TableManagement 類中的 JsonManagedReference 對應
//		private TableManagement table;
//	    
//	 	@NotNull(message = "ReservationDate cannot be null !!!")
//	    @Column(name = "reservation_date", nullable = false)
//	    private LocalDate reservationDate;
//
//	 	@NotNull(message = "ReservationStartTime cannot be null !!!")
//	    @Column(name = "reservation_starttime", nullable = false)
//	    private LocalTime reservationStartTime;
//
//	 	@NotNull(message = "ReservationEndTime cannot be null !!!")
//	    @Column(name = "reservation_endtime", nullable = false)
//	    private LocalTime reservationEndTime;
//	 	
//	 	// 新增桌位狀態欄位
//	    @Enumerated(EnumType.STRING)
//	    @NotNull(message = "Table status cannot be null !!!")
//	    @Column(name = "table_status", nullable = false)
//	    private TableStatus tableStatus; // 定義的枚舉類型
//
//	    // 枚舉類型
//	    public enum TableStatus {
//	        可使用, 訂位中, 用餐中
//	    }
//
//		public ReservationAndTable() {
//			super();
//		}
//
//		public ReservationAndTable(ReservationAndTableId reservationTableId, Reservation reservation,TableManagement table,
//				LocalDate reservationDate, LocalTime reservationStartTime, LocalTime reservationEndTime, TableStatus tableStatus) {
//			super();
//			this.reservationTableId = reservationTableId;
//			this.reservation = reservation;
//			this.table = table;
//			this.reservationDate = reservationDate;
//			this.reservationStartTime = reservationStartTime;
//			this.reservationEndTime = reservationEndTime;
//			this.tableStatus = tableStatus;
//		}
//
//		public ReservationAndTableId getReservationTableId() {
//			return reservationTableId;
//		}
//
//		public void setReservationTableId(ReservationAndTableId reservationTableId) {
//			this.reservationTableId = reservationTableId;
//		}
//
//		public Reservation getReservation() {
//			return reservation;
//		}
//
//		public void setReservation(Reservation reservation) {
//			this.reservation = reservation;
//		}
//
//		public TableManagement getTable() {
//			return table;
//		}
//
//		public void setTable(TableManagement table) {
//			this.table = table;
//		}
//
//		public LocalDate getReservationDate() {
//			return reservationDate;
//		}
//
//		public void setReservationDate(LocalDate reservationDate) {
//			this.reservationDate = reservationDate;
//		}
//
//		public LocalTime getReservationStartTime() {
//			return reservationStartTime;
//		}
//
//		public void setReservationStartTime(LocalTime reservationStartTime) {
//			this.reservationStartTime = reservationStartTime;
//		}
//
//		public LocalTime getReservationEndTime() {
//			return reservationEndTime;
//		}
//
//		public void setReservationEndTime(LocalTime reservationEndTime) {
//			this.reservationEndTime = reservationEndTime;
//		}
//
//		public TableStatus getTableStatus() {
//			return tableStatus;
//		}
//
//		public void setTableStatus(TableStatus tableStatus) {
//			this.tableStatus = tableStatus;
//		}		
//}