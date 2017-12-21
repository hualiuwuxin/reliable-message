package com.smtmvc.goods.service.query;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smtmvc.messageService.model.enume.ConfirmStatus;

public interface GoodsServiceQuery  {
	
	@RequestMapping(value="queryGoods/{uuid}",method=RequestMethod.GET)
	ConfirmStatus queryGoods(@PathVariable("uuid") String uuid);

}
