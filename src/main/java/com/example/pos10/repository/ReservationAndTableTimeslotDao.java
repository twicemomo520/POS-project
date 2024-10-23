package com.example.pos10.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pos10.entity.ReservationAndTableTimeslot;

public interface ReservationAndTableTimeslotDao extends JpaRepository <ReservationAndTableTimeslot, Integer> {
    
	// 1. 將新創建的桌號與營業時間段進行關聯
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO reservation_and_table_timeslot (table_number, operating_hour_id, reservation_date, table_status) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
	public void insertTimeslot(String tableNumber, int operatingHourId, LocalDate reservationDate, String tableStatus);
	
	// 2. 根據桌號查詢相關的預約和時間段 (應用在 TableManagementServiceImpl 中的 deleteTable 方法）
	@Query("SELECT rt FROM ReservationAndTableTimeslot rt WHERE rt.tableNumber = :tableNumber")
    List<ReservationAndTableTimeslot> findByTableNumber(@Param("tableNumber") String tableNumber);
	
	// 3. 將有關聯的桌號與時間段刪除
	@Modifying
	@Transactional
	@Query("DELETE FROM ReservationAndTableTimeslot rt WHERE rt.tableNumber = :tableNumber")
	public void deleteByTableNumber(@Param("tableNumber") String tableNumber);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM ReservationAndTableTimeslot rt WHERE rt.operatingHourId = :operatingHourId")
	public void deleteByOperatingHourId(@Param("operatingHourId") Integer operatingHourId);
	
	// 4. 查詢與營業時間關聯的桌位時間段 (應用在 OperatingHoursServiceImpl 中的 deleteOperatingHours 方法)
	@Query("SELECT rt FROM ReservationAndTableTimeslot rt WHERE rt.operatingHourId = :operatingHourId")
	List<ReservationAndTableTimeslot> findByOperatingHourId(@Param("operatingHourId") Integer operatingHourId);
}
