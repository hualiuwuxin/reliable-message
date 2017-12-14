package com.smtmvc.messageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.smtmvc.messageService.task.ConfirmTasks;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan("com.smtmvc")
public class MessageServiceApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(MessageServiceApplication.class, args);
		
		//初次启动应把 带确认的都 处理一次
		
		
		
		Thread confirmTasksThread = new Thread(  new ConfirmTasks());
		confirmTasksThread.start();
		
		
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
