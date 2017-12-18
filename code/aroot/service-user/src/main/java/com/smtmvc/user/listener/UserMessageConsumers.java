package com.smtmvc.user.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.service.MessageService;
import com.smtmvc.user.model.User;
import com.smtmvc.user.service.impl.UserServiceImpl;

@Component
public class UserMessageConsumers {

	@Autowired
	MessageService messageService;
	
	@Autowired
	UserServiceImpl userService;

	@JmsListener(destination = "user-service-insert")
	public void consume(Message message) {
		User user = JSONObject.parseObject( message.getContent() ,  User.class );
		
		
		if( userService.queryByUUID( user.getUuid() ) == null ) {
			userService.insert( user );
		}
	
		messageService.ack(message.getUuid());
	}

}
