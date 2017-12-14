package com.smtmvc.order.service.query.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smtmvc.messageService.model.enume.ConfirmStatus;
import com.smtmvc.order.dao.OrderMapper;
import com.smtmvc.order.model.Order;
import com.smtmvc.order.service.query.OrderQueryService;

@RestController
public class OrderQueryServiceImpl implements OrderQueryService {
	@Autowired
	OrderMapper orderMapper;

	@GetMapping("queryOrder/{uuid}")
	public ConfirmStatus queryOrder(@PathVariable("uuid") String uuid) {
		Order order = orderMapper.queryByUUID( uuid );
		if( order != null ) {
			return ConfirmStatus.COMPLETE;
		}else {
			return ConfirmStatus.NORECORD;
		}
		
	}

}
