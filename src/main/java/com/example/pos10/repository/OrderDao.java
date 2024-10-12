package com.example.pos10.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Categories;
import com.example.pos10.entity.ComboItems;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;
import com.example.pos10.entity.Orders;

@Repository
public interface OrderDao extends  JpaRepository<Orders, Integer>{
	
	@Query(value = "select o from Orders as o"
			+ " where (o.orderTime between :inputStartDate and :inputEndDate) "
			+ " and (o.checkout = 0)", nativeQuery = false)
	public List<Orders>selectOrderDate(
			@Param("inputStartDate") LocalDateTime startDate,
			@Param("inputEndDate") LocalDateTime endDate);
	
	@Modifying
	@Transactional
	@Query(value = " update orders " 
			+ " set meal_status = '已送達' " 
			+ " where id = :inputId", nativeQuery = true)
	public void updateOrderStatus(
			@Param("inputId") int id);
	
	
	@Modifying
	@Transactional
	@Query(value = "select o from Orders as o where o.mealStatus = '準備中' and o.mealName is NOT NULL", nativeQuery = false)
	public List<Orders> selectInPreparation();

	@Modifying
	@Transactional
	@Query(value = " update orders " + " set meal_status = '待送餐點' " + " where id = :inputId", nativeQuery = true)
	public void updateInPreparation(@Param("inputId") int id);
	

}
