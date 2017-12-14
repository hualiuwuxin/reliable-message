package com.smtmvc.messageService.test;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.model.enume.MessageStatus;
import com.smtmvc.messageService.mq.ActiveMQService;

@RestController
public class Test {
	
	@Autowired
	ActiveMQService activeMQService;
	
	@GetMapping("testSend")
	public String test() throws Exception {
		Message  message = new Message();
		message.setDestination("目的地");
		message.setType("queue");
		message.setConfirmUrl("ConfirmUrl");
		message.setContent("内容");
		message.setStatus( MessageStatus.WAITING_CONFIRM );
		message.setSendTime(0);
		message.setConfirmTime(0);
		message.setUuid(  UUID.randomUUID().toString().replaceAll("-", "") );
		
		activeMQService.send( message );
		
		return "------";
	}
	

}
