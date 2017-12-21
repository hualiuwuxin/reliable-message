package com.smtmvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.smtmvc.dao.GoodsMapper;
import com.smtmvc.messageService.model.Message;
import com.smtmvc.messageService.service.MessageService;
import com.smtmvc.model.Goods;
import com.smtmvc.service.GoodsService;
import com.smtmvc.user.model.User;

@Service
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	GoodsMapper goodsMapper;
	
	@Autowired
	MessageService  messageService;
	
	
	@Transactional(rollbackFor=Exception.class)
	public int insert(Goods goods,int a) {
		User user = new User();
		user.setName("user" + goods.getCount() );
		user.setUuid(  goods.getUuid() );
		Message message = Message.newInstance("user-service-insert","127.0.0.1:7801/queryGoods" ,JSONObject.toJSONString( user ) ,goods.getUuid() );	
		
		//如果这一步出错，抛出异常，事物回滚
		messageService.sendPretreatmentMessage( message  );
		
		
		//如果本地数据库执行失败，前面发的是预消息，并不会真的影响 User 的数据， 预消息会在和查询接口确认后被删除
		int i = goodsMapper.insert( goods  );
		
		
		
		if( a == 1 ) {
			throw new RuntimeException();
		}
		
		
		//这一步，可以出错，所有可以让它这一步永远不会抛出超时，可以异步调用，如果这一步异步没有执行到，因为本地事物已经成功， 查询 可靠消息服务会在和查询接口核对的时候确认 User 应该修改
		//messageService.confirmSend( message.getUuid() );
		
		return i;
	}

	
	

}
