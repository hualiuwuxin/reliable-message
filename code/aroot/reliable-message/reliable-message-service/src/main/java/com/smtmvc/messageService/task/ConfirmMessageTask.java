package com.smtmvc.messageService.task;

import java.util.concurrent.TimeUnit;

import com.smtmvc.messageService.holder.PropertyHolder;
import com.smtmvc.messageService.holder.ServiceHolder;
import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.model.enume.ConfirmStatus;

/**
 * 确认消息任务
 * @author ZHANGYUKUN
 *
 */
public class ConfirmMessageTask  extends MessageTask {
	
	public ConfirmMessageTask( Message message) {
		super( message);
	}
	
	public long calculateSendTime(Message message ) {
		int confirmTime = message.getConfirmTime();
		int interval = 0;
		
		message.getUpdateDate();
		
		if( confirmTime == 0 ) {
			interval = 0;
		}else if( confirmTime == 1 ) {
			interval = 1;
		}else if( confirmTime == 2 ) {
			interval = 2;
		}else if( confirmTime == 3 ) {
			interval = 5;
		}else if( confirmTime == 4 ) {
			interval = 10;
		}else if( confirmTime == 5 ) {
			interval = 30;
		}else if( confirmTime == 6 ) {
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
		
		if( getMessage().getConfirmTime() >= PropertyHolder.getReliableMessageRetry().getMaxConfirmTime() ) {
			
			ServiceHolder.getMessageService().confirmFailureMessage( getMessage().getUuid() );
		}else {
			
			ConfirmStatus confirmStatus = ServiceHolder.getMessageService().httpInvoking( getMessage() );
			ServiceHolder.getMessageService().afterConfirm( confirmStatus, getMessage().getUuid() );
		}
		
	}

	

}
