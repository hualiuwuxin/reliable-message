package com.smtmvc.messageService.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smtmvc.messageService.model.Message;

/**
 * 远程接口
 * @author ZHANGYUKUN
 *
 */
@FeignClient(name = "reliable-message-service")
public interface MessageService {
	
	/**
	 * 发送预消息(这个消息不会不会马上发送,而是会在确认以后发送)
	 * @author ZHANGYUKUN
	 * @param message 
	 */
	@RequestMapping(value="sendPretreatmentMessage",method=RequestMethod.PUT)
	void sendPretreatmentMessage( Message message );
	
	
	
	/**
	 * 确认消息(确认的消息会很快被发送)
	 * 
	 * @author ZHANGYUKUN
	 * @param messageUUID 消息的唯一标志
	 */
	@RequestMapping(value="confirmSend",method=RequestMethod.PUT)
	void confirmSend( String messageUUID );
	
	
	
	/**
	 * 取消消息 (取消的消息会被丢弃 )
	 * 
	 * @author ZHANGYUKUN
	 * @param messageUUID 消息的唯一标志
	 * @return
	 */
	@RequestMapping(value="cancelSend",method=RequestMethod.PUT)
	void cancelSend( String messageUUID );
	
	
	/**
	 * 
	 * 把需要确认的,消息向调用方确认
	 * @param messageUUID
	 * @return
	 */
	@RequestMapping(value="ack",method=RequestMethod.PUT)
	void ack( String messageUUID );
	

}
