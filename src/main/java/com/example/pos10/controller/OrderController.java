package com.example.pos10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.OrderService;

@RestController
@CrossOrigin
public class OrderController {
	
	@Autowired
	private OrderService orderService;

}
