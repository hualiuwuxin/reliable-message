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
	 * 
	 * @param confirmTime 确认次数
	 */
	void scanAndConfirm(Integer  confirmTime);
	
	
	/**
	 * 删除消息
	 * 
	 * @param uuid
	 * @return 影响条数
	 */
	int deleteMessage( String uuid );
	
	/**
	 * 标记消息发送失败
	 * 
	 * @param uuid
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
	 * 
	 * @param message 消息
	 * @param sendStatus 发送状态
	 */
	void addSendRecord(Message message, SendStatus sendStatus);
	
	
	/**
	 * 发送http 请求 
	 * 
	 * @param message
	 * @return
	 */
	ConfirmStatus httpInvoking(Message  message);
	
	/**
	 * 添加确认发送记录
	 * 
	 * @param uuid 
	 * @param confirmStatus 确认的回执状态
	 */
	void addConfirmRecord(String uuid, ConfirmStatus confirmStatus);
	
	/**
	 * 完成确认的后续操作(比如 添加确认发送记录，发送信息到消息队列 )
	 * 
	 * @param confirmStatus 确认的回执状态
	 * @param uuid
	 */
	void afterConfirm(ConfirmStatus confirmStatus, String uuid);
}
