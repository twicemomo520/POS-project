package com.example.pos10.repository;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pos10.entity.CheckoutList;

public interface CheckoutListDao extends JpaRepository<CheckoutList, String>{

	
	@Modifying
    @Transactional
    @Query(value = "INSERT INTO checkout_list (order_id, table_number, total_price, pay_type, checkout, checkout_time) VALUES (:orderId, :tableNumber, :totalPrice, :payType, :checkout, :checkoutTime)", nativeQuery = true)
	Integer insertCheckoutList(@Param("orderId") String orderId,
                              @Param("tableNumber") String tableNumber,
                              @Param("totalPrice") Integer totalPrice,
                              @Param("payType") String payType,
                              @Param("checkout") Boolean checkout,
                              @Param("checkoutTime") LocalDateTime checkoutTime);
    
}
