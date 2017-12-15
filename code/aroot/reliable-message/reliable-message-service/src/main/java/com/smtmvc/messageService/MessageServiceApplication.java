package com.smtmvc.messageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.smtmvc.messageService.mq.ActiveMQService;
import com.smtmvc.messageService.mq.ActiveMQServiceHolder;
import com.smtmvc.messageService.task.ReSendTasks;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan("com.smtmvc")
public class MessageServiceApplication {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext app = SpringApplication.run(MessageServiceApplication.class, args);
		
		
		ActiveMQServiceHolder.setActiveMQService( app.getBean( ActiveMQService.class ) );
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public ReSendTasks confirmTasks() {
		return new ReSendTasks();
	}
	
	
}
