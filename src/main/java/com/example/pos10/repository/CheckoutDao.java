package com.example.pos10.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pos10.entity.Orders;

@Repository
public interface CheckoutDao extends JpaRepository<Orders, Integer> {

	// 查詢特定桌號的未結帳訂單的詳細資料(寫到一半想到更好的寫法 但寫完了
	// 所以不想改了 如果有人想知道再來問就好)
	@Query(value = "SELECT order_id,order_meal_id,combo_name,meal_name,options,price,table_number,checkout FROM orders WHERE table_number = :tableNumber AND checkout = 0", nativeQuery = true)
	List<Object[]> findUnpaidOrdersByTableNumber(@Param("tableNumber") String tableNumber);

	// 查詢特定桌號的未結帳訂單總金額
	@Query(value = "SELECT SUM(price) FROM orders WHERE table_number = :tableNumber AND checkout = 0", nativeQuery = true)
	Integer findTotalUnpaidAmountByTableNumber(@Param("tableNumber") String tableNumber);

	@Modifying
	@Transactional
	@Query(value = "UPDATE orders SET checkout = 1 WHERE order_id = :orderId", nativeQuery = true)
	Integer updateCheckout(@Param("orderId") String orderId);

	@Query(value = "SELECT order_id, order_meal_id, combo_name, meal_name, options, price, table_number, checkout "
			+ "FROM orders WHERE order_id = :orderId " + "UNION ALL "
			+ "SELECT order_id, order_meal_id, combo_name, meal_name, options, price, table_number, checkout "
			+ "FROM orders_history WHERE order_id = :orderId", nativeQuery = true)
	List<Object[]> findUnpaidOrdersByOrderId(@Param("orderId") String orderId);

}
