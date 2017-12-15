package com.smtmvc.messageService.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.mq.ActiveMQServiceHolder;

public class ReSendTask implements Delayed,Runnable {
	
	/**
	 * 执行时间纳秒
	 */
	private Long runTime;
	
	private Message message;
	
	
	
	public ReSendTask(long runTime ,Message message) {
		this.runTime = runTime;
		this.message = message;
	}

	@Override
	public int compareTo(Delayed arg0) {
		ReSendTask reSendTask = (ReSendTask) arg0;
		
		if( this.runTime > reSendTask.runTime ) {
			return 1;
		}else if( this.runTime == reSendTask.runTime ) {
			return 0;
		}else {
			return -1;
		}
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return runTime - System.nanoTime() ;
	}

	@Override
	public void run() {
		ActiveMQServiceHolder.getActiveMQService().send(message);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
