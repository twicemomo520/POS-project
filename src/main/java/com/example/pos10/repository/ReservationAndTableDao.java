//package com.example.pos10.repository;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.example.pos10.entity.Reservation;
//import com.example.pos10.entity.ReservationAndTable;
//import com.example.pos10.entity.ReservationAndTable.TableStatus;
//import com.example.pos10.entity.ReservationAndTableId;
//import com.example.pos10.entity.TableManagement;
//
//public interface ReservationAndTableDao extends JpaRepository<ReservationAndTable, ReservationAndTableId> {
//
//	// 1. 根據 reservationId（預約 ID）或 statuses（桌位狀態）來查詢與該預約相關的所有桌位
//	@Query("SELECT rt.table FROM ReservationAndTable rt WHERE rt.reservation.reservationId = :reservationId " +
//		       "AND (:statuses IS NULL OR rt.tableStatus IN (:statuses))")
//		List <TableManagement> findTablesByReservationIdAndStatus(
//		    @Param ("reservationId") int reservationId, 
//		    @Param ("statuses") List <TableStatus> statuses
//		);
//	
//	// 1-1. 檢查 reservationId 是否存在
//	@Query("SELECT COUNT(rt) > 0 FROM ReservationAndTable rt WHERE rt.reservation.reservationId = :reservationId")
//	boolean existsByReservation_ReservationId (@Param("reservationId") int reservationId);
//	
//	// 2. 根據 tableNumber（桌位號碼）或 statuses（桌位狀態）來查詢與該桌位相關的所有預約
//	@Query("SELECT rt.reservation FROM ReservationAndTable rt WHERE rt.table.tableNumber = :tableNumber AND (:statuses IS NULL OR rt.tableStatus IN (:statuses))")
//	List<Reservation> findReservationsByTableNumberAndStatus(@Param("tableNumber") String tableNumber, @Param("statuses") List<TableStatus> statuses);
//	
//	// 2-1. 檢查 tableNumber 是否存在
//	@Query("SELECT COUNT(t) > 0 FROM TableManagement t WHERE t.tableNumber = :tableNumber")
//	boolean existsByTableNumber(@Param("tableNumber") String tableNumber);
//	
//	// 3. 根據 tableNumber 和 reservationDate 查詢該桌位在指定日期的所有預約
//	@Query("SELECT rt FROM ReservationAndTable rt WHERE rt.table.tableNumber = :tableNumber AND rt.reservationDate = :reservationDate")
//	List<ReservationAndTable> findByTableNumberAndReservationDate(@Param("tableNumber") String tableNumber, @Param("reservationDate") LocalDate reservationDate);
//
//	// 4. 查詢特定日期的已預約桌位
//	@Query("SELECT rt FROM ReservationAndTable rt WHERE rt.reservationDate = :reservationDate")
//	List<ReservationAndTable> findReservedTablesByDate(@Param("reservationDate") LocalDate reservationDate);
//	
//	// 5. 查詢特定日期和時間段內狀態為 "可使用" 的桌位
//	@Query("SELECT rt.table FROM ReservationAndTable rt WHERE rt.reservationDate = :reservationDate " +
//	        "AND rt.reservationStartTime < :reservationEndTime " +
//	        "AND rt.reservationEndTime > :reservationStartTime " +
//	        "AND rt.tableStatus IN (:statuses)")
//	List<TableManagement> findAvailableTablesByDateAndStatus(
//	    @Param("reservationDate") LocalDate reservationDate,
//	    @Param("reservationStartTime") LocalTime reservationStartTime,
//	    @Param("reservationEndTime") LocalTime reservationEndTime,
//	    @Param("statuses") List<TableStatus> statuses
//	);
//    
//    // 5. 查詢指定時間段內的桌位預約情況(TableManagemet)
//    @Query("SELECT rt FROM ReservationAndTable rt WHERE rt.reservationDate = :reservationDate AND rt.reservationStartTime < :slotEnd AND rt.reservationEndTime > :slotStart")
//    List<ReservationAndTable> findReservationsInTimeSlot(
//        @Param("reservationDate") LocalDate reservationDate,
//        @Param("slotStart") LocalTime slotStart,
//        @Param("slotEnd") LocalTime slotEnd
//    ); 
//   
//}