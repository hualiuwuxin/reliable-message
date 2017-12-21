package com.smtmvc.service.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smtmvc.dao.GoodsMapper;
import com.smtmvc.goods.service.query.GoodsServiceQuery;
import com.smtmvc.messageService.model.enume.ConfirmStatus;
import com.smtmvc.model.Goods;


@RestController
public class GoodsServiceQueryImpl implements GoodsServiceQuery {
	
	@Autowired
	GoodsMapper goodsMapper;

	@GetMapping("queryGoods/{uuid}")
	@Override
	public ConfirmStatus queryGoods(@PathVariable("uuid")String uuid) {
		Goods goods = goodsMapper.queryByUUID( uuid );
		
		if( goods != null ) {
			return ConfirmStatus.COMPLETE;
		}else {
			return ConfirmStatus.NORECORD;
		}
		
	}
	

	
	

}
