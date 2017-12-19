package com.smtmvc.messageService.task;

import java.util.concurrent.TimeUnit;

import com.smtmvc.messageService.holder.ServiceHolder;
import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.model.enume.SendStatus;

/**
 * 重发消息任务
 * @author ZHANGYUKUN
 *
 */
public class ReSendMessageTask  extends MessageTask {
	
	

	public ReSendMessageTask( Message message) {
		super( message);
	}
	
	
	public long calculateSendTime(Message message ) {
		
		int sendTime = message.getSendTime();
		
		int interval = 0;
		message.getUpdateDate();
		
		if( sendTime == 0 ) {
			interval = 0;
		}else if( sendTime == 1 ) {
			interval = 1;
		}else if( sendTime == 2 ) {
			interval = 2;
		}else if( sendTime == 3 ) {
			interval = 5;
		}else if( sendTime == 4 ) {
			interval = 10;
		}else if( sendTime == 5 ) {
			interval = 30;
		}else if( sendTime == 6 ) {
			interval = 60;
		}
		
		long intervalMillisecond = interval*60*1000l;
		long delayTimeMillisecond = intervalMillisecond - (System.currentTimeMillis() - message.getUpdateDate().getTime());
		
		if(  delayTimeMillisecond < 0) {
			delayTimeMillisecond = 0;
		}
		
		return (System.nanoTime() + TimeUnit.NANOSECONDS.convert( delayTimeMillisecond ,  TimeUnit.MILLISECONDS ) ) ;
	}
	
	

	@Override
	public void run() {
		SendStatus sendStatus = SendStatus.SUCCEED;
		
		try {
			ServiceHolder.getActiveMQService().send( getMessage() );
		}catch (Exception e) {
			sendStatus= SendStatus.TIMEOUT;
			System.out.println("ActiveMQ出问题了...........");
			e.printStackTrace();
		}
		
		try {
			ServiceHolder.getMessageService().addSendRecord( getMessage() ,sendStatus);
		}catch (Exception e) {
			System.out.println("做发送记录出问题了...........");
			e.getMessage();
		}
	}

	

}
