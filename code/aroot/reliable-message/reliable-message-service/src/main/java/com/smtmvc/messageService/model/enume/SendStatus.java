package com.smtmvc.messageService.model.enume;

public enum SendStatus {
	
	SUCCEED("待确认"),
	TIMEOUT("待发送");
	
	String mark;
	
	SendStatus(String mark  ) {
		this.mark = mark;
	}
}
