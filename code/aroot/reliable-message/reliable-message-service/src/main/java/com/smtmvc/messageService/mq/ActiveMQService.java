package com.smtmvc.messageService.mq;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.smtmvc.messageService.model.Message;

@Component
public class ActiveMQService {

	@Autowired
	private JmsMessagingTemplate jmsTemplate;

	/**
	 * 把消息发送给mq
	 * 
	 * @author ZHANGYUKUN
	 * @param message
	 * @throws Exception
	 */
	public void send(Message message) {
		if (StringUtils.isEmpty(message.getDestination())) {
			throw new RuntimeException("没有目的地");
		}

		Destination destination = new ActiveMQQueue(message.getDestination());
		jmsTemplate.convertAndSend(destination, message );
	}

}
