package com.example.pos10.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pos10.entity.OrderDetailHistory;
import com.example.pos10.entity.OrderHistory;
import com.example.pos10.vo.JoinOrderHistoryVo;

public interface OrderHistoryDao extends JpaRepository<OrderHistory, String>{
	
	 @Query(value = "select * from order_history as o join order_detail_history as od on o.order_id = od.order_id  "
	 		+ " where Function('DATE', o.checkoutTime) between :inputStartDate and :inputEndDate",  nativeQuery = true)
	 public List<JoinOrderHistoryVo> selectDate(
			 @Param("inputStartDate") LocalDate startDate, 
			 @Param ("inputEndDate")LocalDate endDate);

}
