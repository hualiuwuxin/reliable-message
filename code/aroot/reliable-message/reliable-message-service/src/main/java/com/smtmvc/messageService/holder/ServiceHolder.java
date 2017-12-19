package com.smtmvc.messageService.holder;

import com.smtmvc.messageService.service.MessageServiceLocal;
import com.smtmvc.messageService.service.mq.ActiveMQService;

/**
 * 有些地方部方便注入 服务类 ,通过 ServiceHolder 可以很容易在你想得到服务的地方获取服务的实例
 * @author ZHANGYUKUN
 *
 */
public class ServiceHolder {

	private static ActiveMQService activeMQService;
	
	private static MessageServiceLocal messageService;

	public static ActiveMQService getActiveMQService() {
		return activeMQService;
	}

	public static void setActiveMQService(ActiveMQService activeMQService) {
		ServiceHolder.activeMQService = activeMQService;
	}

	public static MessageServiceLocal getMessageService() {
		return messageService;
	}

	public static void setMessageService(MessageServiceLocal messageService) {
		ServiceHolder.messageService = messageService;
	}


}
