package com.smtmvc.messageService.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.smtmvc.messageService.config.ReliableMessageRetry;
import com.smtmvc.messageService.dao.MessageConfirmRecordMapper;
import com.smtmvc.messageService.dao.MessageMapper;
import com.smtmvc.messageService.dao.MessageSendRecordMapper;
import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.model.MessageConfirmRecord;
import com.smtmvc.messageService.model.MessageSendRecord;
import com.smtmvc.messageService.model.enume.ConfirmStatus;
import com.smtmvc.messageService.model.enume.MessageStatus;
import com.smtmvc.messageService.model.enume.SendStatus;
import com.smtmvc.messageService.service.MessageServiceLocal;
import com.smtmvc.messageService.service.mq.ActiveMQService;
import com.smtmvc.messageService.task.ConfirmMessageTask;
import com.smtmvc.messageService.task.MessageTasks;
import com.smtmvc.messageService.task.ReSendMessageTask;

@RestController
public class MessageServiceImpl implements MessageServiceLocal {
	
	@Autowired
	MessageMapper messageMapper;
	
	@Autowired
	MessageConfirmRecordMapper messageConfirmRecordMapper;
	
	@Autowired
	MessageSendRecordMapper messageSendRecordMapper;
	
	@Autowired
	ActiveMQService activeMQService;
	
	@Autowired
	ReliableMessageRetry reliableMessageRetry;
	
	@Autowired
	MessageTasks messageTasks;
	
	@Autowired
	RestTemplate restTemplate;
	

	@PutMapping("sendPretreatmentMessage")
	@Override
	@Transactional
	public void sendPretreatmentMessage(@RequestBody Message message) {
		messageMapper.insert( message );
	}

	@PutMapping("confirmSend")
	@Override
	@Transactional
	public void confirmSend(@RequestBody String messageUUID)  {
		
		Message messgae = 	messageMapper.selectByUUID( messageUUID );
		if(  messgae == null ) {
			throw new RuntimeException("无效的UUID");
		}
		messageMapper.changeStatus(messageUUID,MessageStatus.WAITING_SEND);
		
		sendToMQ( messgae );
		
		messageMapper.changeStatus(messageUUID,MessageStatus.SENDED);
		
	}

	@PutMapping("cancelSend")
	@Override
	@Transactional
	public void cancelSend(@RequestBody String messageUUID)  {
		
		Message messgae = 	messageMapper.selectByUUID( messageUUID );
		if(  messgae == null ) {
			throw new RuntimeException("无效的UUID");
		}
		
		messageMapper.deleteByUUID(messageUUID);
		
	}
	
	
	@PutMapping("ack")
	@Override
	@Transactional
	public void ack(@RequestBody String uuid)  {
		Message messgae = 	messageMapper.selectByUUID( uuid );
		if(  messgae == null ) {
			throw new RuntimeException("无效的UUID");
		}
		
		messageMapper.changeStatus(uuid,MessageStatus.SEND_SUCCEED);
	}

	@Override
	public void sendToMQ(Message message)  {
		
		if( message.getSendTime() >= reliableMessageRetry.getMaxSendTime() ) {
			sendFailure( message.getUuid() );
			return;
		}
		
		ReSendMessageTask reSendMessageTask = new ReSendMessageTask(  message );
		
		if( !messageTasks.contains( reSendMessageTask ) ) {
			messageTasks.put( reSendMessageTask );
		}
		
	}

	@Override
	public void addSendRecord(Message message,SendStatus sendStatus) {
		
		if(SendStatus.SUCCEED.equals(  sendStatus )  ) {
			messageMapper.addSendTime( message.getUuid() );
			message.setSendTime( message.getSendTime()+1  );
		}
		
		
		MessageSendRecord messageConfirmRecord = MessageSendRecord.newInstance( message );
		messageConfirmRecord.setStatus( sendStatus );
		messageSendRecordMapper.insert( messageConfirmRecord );
		
	}
	

	@Override
	public void scanAndReSendToMQ(Integer  sendTime) {
		int pageNum = 0;
		int pageSize = 10;
		
		Page<Message> page = new Page<>( pageNum , pageSize );
		do {
			page.setPageNum( page.getPageNum() + 1 );
	        PageHelper.startPage( page.getPageNum() ,  page.getPageSize() );
	        
	        page = (Page<Message>)  messageMapper.querySendedByStatus( MessageStatus.SENDED,sendTime );
	        
			for(Message message :  page) {
				sendToMQ(message);
			}
		}while( page.getEndRow() < page.getTotal() );
		
	}
	
	
	
	

	@Override
	public void scanAndConfirm(Integer  confirmTime) {
		
		int pageNum = 0;
		int pageSize = 10;
		
		Page<Message> page = new Page<>( pageNum , pageSize );
		do {
			page.setPageNum( page.getPageNum() + 1 );
			
	        PageHelper.startPage( page.getPageNum() ,  page.getPageSize() );
	        
	        page = (Page<Message>)messageMapper.queryWaitingConfirmByStatus( MessageStatus.WAITING_CONFIRM , confirmTime );
	        
	        for(Message message : page  ) {
				tryConfirm( message);
			}
			 
		}while( page.getEndRow() < page.getTotal() );
		
	}
	
	
	
	/**
	 * 没有打到最大尝试次数的就去确认,到达最大次数的就标记未过期
	 * @param message
	 */
	private void tryConfirm(Message message ) {
		
		ConfirmMessageTask confirmMessageTask = new ConfirmMessageTask( message );
		
		if( !messageTasks.contains( confirmMessageTask ) ) {
			messageTasks.put( confirmMessageTask );
		}
		
	}
	
	/**
	 * 询问以后的处理 
	 * @param confirmStatus
	 * @param uuid
	 */
	@Transactional
	public void afterConfirm(ConfirmStatus confirmStatus, String uuid) {
		
		addConfirmRecord(uuid,confirmStatus);
		
		if( ConfirmStatus.COMPLETE.equals(  confirmStatus ) ) {
			
			confirmSend(uuid);
		}else if( ConfirmStatus.CANCEL.equals(  confirmStatus ) ) {
			
			deleteMessage(uuid);
		}else if( ConfirmStatus.NORECORD.equals(  confirmStatus ) ){
			
			deleteMessage(uuid);
		}else {
			throw new RuntimeException("不知道的类型");
		}
	}
	
	/**
	 * 做询问记录
	 * 备注：必须执行在内嵌事物中
	 *   
	 * @param uuid
	 * @param confirmStatus 
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	@Override
	public void addConfirmRecord(String uuid, ConfirmStatus confirmStatus) {
		Message messgae = 	messageMapper.selectByUUID( uuid );
		
		if(  messgae != null ) {
			messageMapper.addConfirmTime( uuid );
			messgae.setConfirmTime( messgae.getConfirmTime() + 1 );
		}
		
		MessageConfirmRecord messageConfirmRecord = MessageConfirmRecord.newInstance(messgae);
		messageConfirmRecord.setStatus(  confirmStatus  );
		messageConfirmRecordMapper.insert( messageConfirmRecord );
		
	}
	
	
	@Override
	public ConfirmStatus httpInvoking(Message  message) {
		String url ="http://" + message.getConfirmUrl()+"/" + message.getUuid();
		
		System.out.println( "我调用了：" + url );
		
		System.out.println( "http 请求已发送:" + url );
		return restTemplate.getForObject(url, ConfirmStatus.class );
	}

	@Override
	public int deleteMessage(String uuid) {
		return messageMapper.deleteByUUID(uuid);
	}

	@Override
	public int sendFailure(String uuid) {
		return messageMapper.changeStatus(uuid, MessageStatus.SEND_FAILURE);
	}

	@Override
	public int confirmFailureMessage(String uuid) {
		return messageMapper.changeStatus(uuid, MessageStatus.CONFIRM_FAILURE);
	}
	
	
	
	
	
	
	
	

}
