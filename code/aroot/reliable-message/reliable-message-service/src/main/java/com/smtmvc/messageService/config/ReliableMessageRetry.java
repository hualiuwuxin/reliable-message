package com.smtmvc.messageService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reliableMessage.retry")
public class ReliableMessageRetry {
	
	
	private int maxConfirmTime;
	
	
	
	private int maxSendTime;


	/**
	 * 最大确认次数
	 */
	public int getMaxConfirmTime() {
		return maxConfirmTime;
	}


	public void setMaxConfirmTime(int maxConfirmTime) {
		this.maxConfirmTime = maxConfirmTime;
	}

	
	/**
	 * 最大重发次数
	 */
	public int getMaxSendTime() {
		return maxSendTime;
	}


	public void setMaxSendTime(int maxSendTime) {
		this.maxSendTime = maxSendTime;
	}
	
	
	
	


}
