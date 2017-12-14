package com.smtmvc.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.service.MessageService;
import com.smtmvc.order.model.Order;
import com.smtmvc.order.service.OrderService;
import com.smtmvc.user.model.User;
import com.smtmvc.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MessageService  messageService;

	@GetMapping("test")
	public String test() {
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "") ;
		int count = (int) (Math.random()*100) ;
		User user = new User();
		user.setName("user" + count );
		user.setUuid(  uuid );
		
		Order order  = new Order();
		order.setCount(Long.parseLong( count+"" ));
		order.setUuid( uuid );
		
		
		Message  message = Message.newInstance();
		message.setDestination("user-service-insert");
		message.setConfirmUrl("127.0.0.1:7701/queryOrder");
		message.setContent(  JSONObject.toJSONString( user ) );
		message.setUuid( uuid );
		
		
		
		
		
		messageService.sendPretreatmentMessage( message  );
		
		
		Long orderId = orderService.insert(order);
		
		messageService.confirmSend( message.getUuid() );
		
		
		return "orderId:" +orderId ;
	}

}
