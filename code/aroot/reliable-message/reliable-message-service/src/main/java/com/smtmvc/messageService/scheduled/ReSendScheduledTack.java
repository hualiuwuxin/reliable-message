package com.smtmvc.messageService.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smtmvc.messageService.service.MessageServiceLocal;

@Component
public class ReSendScheduledTack {

	@Autowired
	MessageServiceLocal messageService;


	@Scheduled(fixedRate=1*60*1000 )
	public void cronSend1() {
		messageService.scanAndReSendToMQ(1);
	}

	@Scheduled(fixedRate=2*60*1000 )
	public void cronSend2() {
		messageService.scanAndReSendToMQ(2);
	}

	@Scheduled(fixedRate=5*60*1000 )
	public void cronSend3() {
		messageService.scanAndReSendToMQ(3);
	}

	@Scheduled(fixedRate=10*60*1000 )
	public void cronSend4() {
		messageService.scanAndReSendToMQ(4);
	}

	@Scheduled(fixedRate=30*60*1000 )
	public void cronSend5() {
		messageService.scanAndReSendToMQ(5);
	}

	@Scheduled(fixedRate=60*60*1000 )
	public void cronSend6() {
		messageService.scanAndReSendToMQ(6);
	}


}
