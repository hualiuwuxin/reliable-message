package com.smtmvc.messageService.service;

import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.model.enume.ConfirmStatus;
import com.smtmvc.messageService.model.enume.SendStatus;

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
	void scanAndReSendToMQ(Integer  sendTime);
	
	
	/**
	 * 扫描应该应该确认的,然后询问一次
	 */
	void scanAndConfirm(Integer  confirmTime);
	
	
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
	int confirmFailureMessage(String uuid );


	/**
	 * 做发送记录
	 * @param message
	 */
	void addSendRecord(Message message, SendStatus sendStatus);
	
	
	/**
	 * 发送http 请求 
	 * @param message
	 * @return
	 */
	ConfirmStatus httpInvoking(Message  message);
	
	/**
	 * 添加确认发送记录
	 * @param uuid
	 * @param confirmStatus
	 */
	void addConfirmRecord(String uuid, ConfirmStatus confirmStatus);
	
	/**
	 * 确认以后
	 * @param confirmStatus
	 * @param uuid
	 */
	void afterConfirm(ConfirmStatus confirmStatus, String uuid);
}
