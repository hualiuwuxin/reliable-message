package com.smtmvc.messageService.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
		
		SendStatus sendStatus = SendStatus.SUCCEED;
		try {
			activeMQService.send( message );
		}catch (Exception e) {
			sendStatus= SendStatus.TIMEOUT;
		}
		
		addSendRecord( message,sendStatus);
	}

	/**
	 * 做发送记录
	 * @param message
	 */
	private void addSendRecord(Message message,SendStatus sendStatus) {
		
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
			sendToMQ( message );
		}
		
	}

	@Override
	public void scanAndConfirm() {
		List<Message> list =  messageMapper.queryByStatus( MessageStatus.WAITING_CONFIRM );
		for(Message message : list  ) {
			
			//下面应该异步,多线程
			String url ="http://" + message.getConfirmUrl()+"/" + message.getUuid();
			ConfirmStatus confirmStatus = httpInvoking( url );
			
			afterConfirm( confirmStatus, message.getUuid() );
		}
		
		
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
	 * @param url
	 */
	private ConfirmStatus httpInvoking(String  url) {
		System.out.println( "http 请求已发送:" + url );
		return restTemplate.getForObject(url, ConfirmStatus.class );
	}

	@Override
	public int deleteMessage(String uuid) {
		return messageMapper.deleteByUUID(uuid);
	}
	
	
	
	
	
	
	
	

}
