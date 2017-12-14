package com.smtmvc.user.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smtmvc.user.model.User;

@FeignClient(name = "service-user")
public interface UserService {

	@RequestMapping(value="insert",method=RequestMethod.POST)
	Long insert(User user);

}
