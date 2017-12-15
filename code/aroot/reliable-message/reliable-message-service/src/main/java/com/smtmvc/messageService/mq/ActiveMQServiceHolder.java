package com.smtmvc.messageService.mq;

public class ActiveMQServiceHolder {

	private static ActiveMQService activeMQService;

	public static ActiveMQService getActiveMQService() {
		return activeMQService;
	}

	public static void setActiveMQService(ActiveMQService activeMQService) {
		ActiveMQServiceHolder.activeMQService = activeMQService;
	}

}
