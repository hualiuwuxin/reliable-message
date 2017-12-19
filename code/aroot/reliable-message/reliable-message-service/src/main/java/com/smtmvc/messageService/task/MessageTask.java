package com.smtmvc.messageService.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.smtmvc.messageService.model.Message;

public abstract class MessageTask implements Delayed,Runnable {
	
	/**
	 * 执行时间纳秒
	 */
	private Long runTime;
	
	/**
	 * 消息对象
	 */
	private Message message;
	
	
	public MessageTask(Message message) {
		this.runTime = calculateSendTime( message);
		this.message = message;
	}
	
	/**
	 * 计算发送时间
	 * @param message
	 * @return
	 */
	abstract long calculateSendTime(Message message ) ;
	
	
	@Override
	public int compareTo(Delayed arg0) {
		MessageTask reSendTask = (MessageTask) arg0;
		
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
		MessageTask other = (MessageTask) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

}
