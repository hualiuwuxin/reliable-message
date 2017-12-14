package com.smtmvc.order.service.query;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smtmvc.messageService.model.enume.ConfirmStatus;

@FeignClient(name = "service-order")
public interface OrderQueryService {

	@RequestMapping(value="queryOrder/{uuid}",method=RequestMethod.GET)
	ConfirmStatus queryOrder(@PathVariable("uuid") String uuid);

}
