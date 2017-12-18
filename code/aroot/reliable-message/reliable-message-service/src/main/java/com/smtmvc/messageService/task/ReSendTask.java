package com.smtmvc.messageService.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.model.enume.SendStatus;
import com.smtmvc.messageService.mq.ServiceHolder;

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
		SendStatus sendStatus = SendStatus.SUCCEED;
		
		try {
			ServiceHolder.getActiveMQService().send(message);
		}catch (Exception e) {
			sendStatus= SendStatus.TIMEOUT;
			System.out.println("ActiveMQ出问题了...........");
			e.printStackTrace();
		}
		
		try {
			ServiceHolder.getMessageService().addSendRecord( message,sendStatus);
		}catch (Exception e) {
			System.out.println("做发送记录出问题了...........");
			e.getMessage();
		}
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReSendTask other = (ReSendTask) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

}
