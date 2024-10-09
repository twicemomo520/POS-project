package com.example.pos10.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.service.ifs.OrderService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.InsertAuthorizationReq;
import com.example.pos10.vo.SearchOrderReq;
import com.example.pos10.vo.SearchOrderStatusRes;
import com.example.pos10.vo.UpdateOrderReq;

@RestController
@CrossOrigin
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
    @PostMapping("/pos/searchOrderStatus")
    public SearchOrderStatusRes searchOrderStatus(@RequestBody @Valid  SearchOrderReq req) {
        return orderService.searchOrderStatus(req);
    }
    
    @PostMapping("/pos/updateOrderStatus")
    public BasicRes searchOrderStatus(@RequestBody @Valid  UpdateOrderReq req) {
    	return orderService.updateOrderStatus(req);
    }

}
