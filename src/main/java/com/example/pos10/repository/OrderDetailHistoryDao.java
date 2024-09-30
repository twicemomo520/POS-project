package com.example.pos10.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pos10.entity.OrderDetailHistory;
import com.example.pos10.entity.OrderHistory;
import com.example.pos10.vo.JoinOrderVo;

public interface OrderDetailHistoryDao extends JpaRepository<OrderDetailHistory, Integer>{
	
	 @Query(value = "select new com.example.pos10.vo.JoinOrderVo(od.id, od.orderId, od.comboName, od.mealDetail, od.quantity, od.price, o.checkoutTime)"
	 		+ " from OrderDetailHistory as od  "
	 		+ " join OrderHistory as o on od.orderId = o.orderId "
	 		+ " where (:inputStartDate is null or :inputEndDate is null or o.checkoutTime between :inputStartDate and :inputEndDate)",  nativeQuery = false)
	 public List<JoinOrderVo> selectDate(
			 @Param("inputStartDate") LocalDateTime startDate, 
			 @Param ("inputEndDate")LocalDateTime endDate);
}
