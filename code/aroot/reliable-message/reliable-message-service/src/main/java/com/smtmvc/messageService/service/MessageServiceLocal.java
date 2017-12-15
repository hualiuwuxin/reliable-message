package com.smtmvc.messageService.service;

import com.smtmvc.messageService.model.Message;

/**
 * 本地接口
 * @author ZHANGYUKUN
 *
 */
public interface MessageServiceLocal extends MessageService {
	
	/**
	 * 发送消息到
	 * @param message
	 */
	void sendToMQ(Message message);
	
	
	
	/**
	 * 把应该重发到mq 的重发到mq
	 */
	void scanAndReSendToMQ();
	
	
	/**
	 * 扫描应该应该确认的,然后询问一次
	 */
	void scanAndConfirm();
	
	
	/**
	 * 删除消息
	 * @param uuid
	 * @return 影响条数
	 */
	int deleteMessage( String uuid );
	
	/**
	 * 发送失败
	 * 
	 * @return
	 */
	int sendFailure(String uuid );
	
	
	/**
	 * 确认失败
	 * 
	 * @param uuid
	 * @return
	 */
	int confirmMessage(String uuid );
	
	

}
