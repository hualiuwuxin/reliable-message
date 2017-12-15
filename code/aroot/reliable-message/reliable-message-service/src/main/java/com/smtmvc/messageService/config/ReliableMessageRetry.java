package com.smtmvc.messageService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reliableMessage.retry")
public class ReliableMessageRetry {
	
	/**
	 * 最大确认次数
	 */
	private int maxConfirmTime;
	
	
	/**
	 * 最大重发次数
	 */
	private int maxSendTime;


	public int getMaxConfirmTime() {
		return maxConfirmTime;
	}


	public void setMaxConfirmTime(int maxConfirmTime) {
		this.maxConfirmTime = maxConfirmTime;
	}


	public int getMaxSendTime() {
		return maxSendTime;
	}


	public void setMaxSendTime(int maxSendTime) {
		this.maxSendTime = maxSendTime;
	}
	
	
	
	


}
