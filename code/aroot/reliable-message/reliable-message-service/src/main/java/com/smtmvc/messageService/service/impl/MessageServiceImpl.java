package com.smtmvc.messageService.service.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
import com.smtmvc.messageService.mq.ActiveMQService;
import com.smtmvc.messageService.service.MessageServiceLocal;
import com.smtmvc.messageService.task.ReSendTask;
import com.smtmvc.messageService.task.ReSendTasks;

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
	ReSendTasks reSendTasks;
	

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
	public void ack(@RequestBody String messageUUID)  {
		Message messgae = 	messageMapper.selectByUUID( messageUUID );
		if(  messgae == null ) {
			throw new RuntimeException("无效的UUID");
		}
		
		messageMapper.changeStatus(messageUUID,MessageStatus.SEND_SUCCEED);
	}

	@Override
	public void sendToMQ(Message message)  {
		
		
		if( message.getSendTime() >= reliableMessageRetry.getMaxSendTime() ) {
			sendFailure( message.getUuid() );
			return;
		}
		
		ReSendTask reSendTask = new ReSendTask(  calculateSendTime( message.getSendTime() )  , message );
		
		if( !reSendTasks.contains( reSendTask ) ) {
			reSendTasks.put( reSendTask );
		}
		
	}

	@Override
	public void addSendRecord(Message message,SendStatus sendStatus) {
		
		if(SendStatus.SUCCEED.equals(  sendStatus )  ) {
			messageMapper.addSendTime( message.getUuid() );
			message.setSendTime( message.getSendTime()  );
		}
		
		
		MessageSendRecord messageConfirmRecord = MessageSendRecord.newInstance( message );
		messageConfirmRecord.setStatus( sendStatus );
		messageSendRecordMapper.insert( messageConfirmRecord );
		
	}
	

	@Override
	public void scanAndReSendToMQ() {
		List<Message> list =  messageMapper.queryByStatus( MessageStatus.SENDED );
		
		for(Message message :  list) {
			sendToMQ(message);
		}
		
	}
	
	/**
	 * 计算发送时间
	 * @return
	 */
	private long calculateSendTime(int count ) {
		
		int interval = 0;
		
		if( count == 0 ) {
			interval = 0;
		}else if( count == 1 ) {
			interval = 1;
		}else if( count == 2 ) {
			interval = 2;
		}else if( count == 3 ) {
			interval = 5;
		}else if( count == 4 ) {
			interval = 10;
		}else if( count == 5 ) {
			interval = 30;
		}else if( count == 6 ) {
			interval = 60;
		}
		
		return TimeUnit.NANOSECONDS.convert( interval ,  TimeUnit.MINUTES ) ;
	}
	
	

	@Override
	public void scanAndConfirm() {
		List<Message> list =  messageMapper.queryByStatus( MessageStatus.WAITING_CONFIRM );
		for(Message message : list  ) {
			tryConfirm( message );
		}
		
		
	}
	
	
	ExecutorService confirmThreadPool = Executors.newFixedThreadPool(10);
	
	/**
	 * 没有打到最大尝试次数的就去确认,到达最大次数的就标记未过期
	 * @param message
	 */
	private void tryConfirm(Message message  ) {
		
		confirmThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				
				if( message.getConfirmTime() >= reliableMessageRetry.getMaxConfirmTime() ) {
					
					confirmMessage( message.getUuid() );
				}
				
				ConfirmStatus confirmStatus = httpInvoking( message );
				afterConfirm( confirmStatus, message.getUuid() );
				
			}
		});
		
	}
	
	/**
	 * 询问以后的处理 
	 * @param confirmStatus
	 * @param uuid
	 */
	@Transactional
	private void afterConfirm(ConfirmStatus confirmStatus, String uuid) {
		
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
	 * @param uuid
	 * @param confirmStatus 
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	private void addConfirmRecord(String uuid, ConfirmStatus confirmStatus) {
		Message messgae = 	messageMapper.selectByUUID( uuid );
		if(  messgae == null ) {
			throw new RuntimeException("无效的UUID");
		}

		
		messageMapper.addConfirmTime( uuid );
		
		MessageConfirmRecord messageConfirmRecord = MessageConfirmRecord.newInstance(messgae);
		messageConfirmRecord.setStatus(  confirmStatus  );
		messageConfirmRecordMapper.insert( messageConfirmRecord );
		
	}
	
	@Autowired
	RestTemplate restTemplate;

	/**
	 * 发送http 请求 ,暂时还没实现
	 * @param message
	 */
	private ConfirmStatus httpInvoking(Message  message) {
		String url ="http://" + message.getConfirmUrl()+"/" + message.getUuid();
		
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
	public int confirmMessage(String uuid) {
		return messageMapper.changeStatus(uuid, MessageStatus.CONFIRM_FAILURE);
	}
	
	
	
	
	
	
	
	

}
