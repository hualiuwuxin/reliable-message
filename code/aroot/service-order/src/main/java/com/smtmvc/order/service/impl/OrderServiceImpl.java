package com.smtmvc.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smtmvc.order.dao.OrderMapper;
import com.smtmvc.order.model.Order;
import com.smtmvc.order.service.OrderService;

@RestController
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	OrderMapper orderDao;
	
	@PostMapping("insert")
	public Long insert(@RequestBody Order order) {
		
		if(  orderDao.insert( order ) == 1 ) {
			return order.getId();
		}else {
			return -1l;
		}
	}

}
