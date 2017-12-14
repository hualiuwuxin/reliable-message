package com.smtmvc.messageService.model.enume;

/**
 * 用于  预消息  应该如何处理的 的枚举
 * @author ZHANGYUKUN
 *
 */
public enum ConfirmStatus {
	
	COMPLETE("完成"),
	CANCEL("取消"),
	NORECORD("没有记录");
	
	String mark;
	
	ConfirmStatus(String mark  ) {
		this.mark = mark;
	}
}
