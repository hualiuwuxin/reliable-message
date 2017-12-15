package com.smtmvc.messageService.service.fallback;

import org.springframework.stereotype.Component;

import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.service.MessageService;

@Component
public class MessageServiceFallback implements MessageService {

	@Override
	public void sendPretreatmentMessage(Message message) {
		
	}

	@Override
	public void confirmSend(String messageUUID) {
		System.out.println("confirmSend 被短路------------");
	}

	@Override
	public void cancelSend(String messageUUID) {
		
	}

	@Override
	public void ack(String messageUUID) {
		
	}

}
