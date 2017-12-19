package com.smtmvc.messageService.holder;

import com.smtmvc.messageService.config.ReliableMessageRetry;

public class PropertyHolder {
	
	private static ReliableMessageRetry reliableMessageRetry;

	public static ReliableMessageRetry getReliableMessageRetry() {
		return reliableMessageRetry;
	}

	public static void setReliableMessageRetry(ReliableMessageRetry reliableMessageRetry) {
		PropertyHolder.reliableMessageRetry = reliableMessageRetry;
	}

}
