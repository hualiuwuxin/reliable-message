package com.smtmvc.messageService.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smtmvc.messageService.service.MessageServiceLocal;

@Component
public class ScheduTack {
	
	@Autowired
	MessageServiceLocal messageService;
	
	@Scheduled(cron="0 * * * * *")
    public void cronConfirm(){
		System.out.println( "确认定时任务执行------------------" );
		messageService.scanAndConfirm();
    }
	
	@Scheduled(cron="30 * * * * *")
    public void cronSend(){
		System.out.println( "重发任务执行------------------" );
		messageService.scanAndReSendToMQ();
    }



}
