package com.smtmvc.messageService;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.PageHelper;
import com.smtmvc.messageService.config.ReliableMessageRetry;
import com.smtmvc.messageService.holder.PropertyHolder;
import com.smtmvc.messageService.holder.ServiceHolder;
import com.smtmvc.messageService.service.MessageServiceLocal;
import com.smtmvc.messageService.service.mq.ActiveMQService;
import com.smtmvc.messageService.task.MessageTasks;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan("com.smtmvc")
public class MessageServiceApplication {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext app = SpringApplication.run(MessageServiceApplication.class, args);
		
		
		ServiceHolder.setActiveMQService( app.getBean( ActiveMQService.class ) );
		ServiceHolder.setMessageService( app.getBean( MessageServiceLocal.class ) );
		
		
		PropertyHolder.setReliableMessageRetry( app.getBean( ReliableMessageRetry.class ) );
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public MessageTasks messageTasks() {
		return new MessageTasks();
	}
	
	
	@Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","true");
        properties.setProperty("rowBoundsWithCount","true");
        properties.setProperty("reasonable","true");
        properties.setProperty("dialect","mysql");    //配置mysql数据库的方言
        pageHelper.setProperties(properties);
        return pageHelper;
    }
	
	
}
