package com.smtmvc.order.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smtmvc.order.model.Order;

@FeignClient(name = "service-order")
public interface OrderService {

	@RequestMapping(value="insert",method=RequestMethod.POST)
	Long insert(Order order);


}
