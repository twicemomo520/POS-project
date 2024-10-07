package com.example.pos10.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos10.entity.Orders;
import com.example.pos10.entity.OrdersId;


public interface OrderDao extends  JpaRepository<Orders, OrdersId>{

}
