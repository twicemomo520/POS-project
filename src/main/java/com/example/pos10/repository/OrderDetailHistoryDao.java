package com.example.pos10.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.ComboItems;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.OrderDetailHistory;
import com.example.pos10.entity.OrderHistory;
import com.example.pos10.vo.JoinOrderHistoryVo;

@Repository
public interface OrderDetailHistoryDao extends JpaRepository<OrderDetailHistory, Integer>{
	
	 @Query(value = "select new com.example.pos10.vo.JoinOrderHistoryVo"
	 		+ " (od.id, od.orderId, od.orderMealId, od.comboName, od.mealDetail, od.workstationId, "
	 		+ " od.quantity, od.price, od.mealStatus, od.tableNumber, od.orderTime, od.checkout, "
	 		+ " o.totalPrice, o.payType, o.checkoutTime, o.memberAccount) "
//	 		+ " SUM(od.price) as totalRevenue,  COUNT(od.orderId) as totalOrders)"
	 		+ " from OrderDetailHistory as od  "
	 		+ " join OrderHistory as o on od.orderId = o.orderId "
	 		+ " where (:inputStartDate is null or :inputEndDate is null or o.checkoutTime between :inputStartDate and :inputEndDate)"
	 		+ " order by o.checkoutTime ASC ",  nativeQuery = false)
	 public List<JoinOrderHistoryVo> searchOrderDetailHistory(
			 @Param("inputStartDate") LocalDateTime startDate, 
			 @Param ("inputEndDate")LocalDateTime endDate);
	 
	 @Query(value = " select c from ComboItems as c"
	 		+ " where c.comboName = :inputMenuName", nativeQuery = false)
	 public ComboItems findComboItems(
			 @Param("inputMenuName") String menuName);
	 
	 @Query(value = " select m from MenuItems as m"
		 		+ " where m.mealName = :inputMenuName", nativeQuery = false)
		 public MenuItems findMenuItems(
				 @Param("inputMenuName") String menuName);
	 
}
