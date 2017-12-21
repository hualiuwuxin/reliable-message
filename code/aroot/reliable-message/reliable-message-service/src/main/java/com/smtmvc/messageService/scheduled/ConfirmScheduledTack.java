package com.smtmvc.messageService.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smtmvc.messageService.service.MessageServiceLocal;

@Component
public class ConfirmScheduledTack {
	
	@Autowired
	MessageServiceLocal messageService;


	@Scheduled(fixedRate=1*60*1000,initialDelay=0 )
	public void cronConfirm1() {
		messageService.scanAndConfirm(1);
	}

	@Scheduled(fixedRate=2*60*1000,initialDelay=5 )
	public void cronConfirm2() {
		messageService.scanAndConfirm(2);
	}

	@Scheduled(fixedRate=5*60*1000,initialDelay=10 )
	public void cronConfirm3() {
		messageService.scanAndConfirm(3);
	}

	@Scheduled(fixedRate=10*60*1000,initialDelay=15 )
	public void cronConfirm4() {
		messageService.scanAndConfirm(4);
	}

	@Scheduled(fixedRate=30*60*1000 , initialDelay=20)
	public void cronConfirm5() {
		messageService.scanAndConfirm(5);
	}

	@Scheduled(fixedRate=60*60*1000,initialDelay=25 )
	public void cronConfirm6() {
		messageService.scanAndConfirm(6);
	}
	
	
	


}
