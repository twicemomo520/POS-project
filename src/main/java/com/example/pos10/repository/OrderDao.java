package com.example.pos10.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Orders;
import com.example.pos10.entity.OrdersId;

@Repository
public interface OrderDao extends  JpaRepository<Orders, OrdersId>{
	
	@Query(value = "select o from Orders as o"
			+ " where (o.orderTime between :inputStartDate and :inputEndDate) "
			+ " and (o.checkout = 0)", nativeQuery = false)
	public List<Orders>selectOrderDate(
			@Param("inputStartDate")LocalDateTime startDate,
			@Param("inputEndDate")LocalDateTime endDate);
	

}
