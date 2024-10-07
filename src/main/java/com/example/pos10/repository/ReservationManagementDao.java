package com.example.pos10.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.ReservationManagement;
import com.example.pos10.entity.TableManagement;

@Repository
public interface ReservationManagementDao extends JpaRepository<ReservationManagement, Integer> {

    // 1. 查詢可用桌位 (桌位狀態為 AVAILABLE，且無重疊預訂時間)
	@Query ("SELECT t FROM TableManagement t WHERE t.tableStatus = 'AVAILABLE' " +
		    "AND NOT EXISTS (SELECT r FROM ReservationManagement r WHERE r.tableManagement = t " +
		    "AND r.reservationDate = :reservationDate " +
			"AND (:startTime < r.reservationEndingTime AND :endTime > r.reservationStarttime))")
		List <TableManagement> findAvailableTables ( @Param ("reservationDate") LocalDate reservationDate, 
		                                             @Param ("startTime") LocalTime startTime, 
		                                             @Param ("endTime") LocalTime endTime);
}