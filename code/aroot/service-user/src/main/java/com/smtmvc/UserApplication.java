package com.smtmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import com.smtmvc.user.service.impl.UserServiceImpl;

@SpringBootApplication
@EnableDiscoveryClient

@EnableEurekaClient
@EnableFeignClients
public class UserApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(UserApplication.class, args);
	}

}
