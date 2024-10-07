package com.example.pos10.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Orders;
import com.example.pos10.vo.CheckoutDetailRes;

@Repository
public interface CheckoutDao extends JpaRepository<Orders, Integer> {

	// 查詢特定桌號的未結帳訂單的詳細資料
    @Query(value = "SELECT order_id,order_meal_id,combo_name,meal_name,options,price,table_number,checkout FROM orders WHERE table_number = :tableNumber AND checkout = 0", nativeQuery = true)
    List<CheckoutDetailRes> findUnpaidOrdersByTableNumber(@Param("tableNumber") String tableNumber);

    // 查詢特定桌號的未結帳訂單總金額
    @Query(value = "SELECT SUM(price) FROM orders WHERE table_number = :tableNumber AND checkout = 0", nativeQuery = true)
    Integer findTotalUnpaidAmountByTableNumber(@Param("tableNumber") String tableNumber);
	
}
