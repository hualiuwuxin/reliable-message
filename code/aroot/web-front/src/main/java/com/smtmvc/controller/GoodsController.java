package com.smtmvc.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smtmvc.messageService.service.MessageService;
import com.smtmvc.model.Goods;
import com.smtmvc.order.service.OrderService;
import com.smtmvc.service.GoodsService;
import com.smtmvc.user.service.UserService;

@RestController
public class GoodsController {

	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MessageService  messageService;

	@Autowired
	GoodsService goodsService;
	
	
	@GetMapping("test")
	public String test(int a) {
		
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "") ;
		int count = (int) (Math.random()*100) ;
		
		Goods goods  = new Goods();
		goods.setCount(Long.parseLong( count+"" ));
		goods.setUuid( uuid );
		
		
		goodsService.insert(goods,a);
		
		
		return "OK";
	}
	

}
