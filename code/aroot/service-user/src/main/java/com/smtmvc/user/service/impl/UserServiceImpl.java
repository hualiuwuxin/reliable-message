package com.smtmvc.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smtmvc.user.dao.UserMapper;
import com.smtmvc.user.model.User;
import com.smtmvc.user.service.UserService;

@RestController
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserMapper userDao;
	
	@PostMapping("insert")
	public Long insert(@RequestBody User user) {
		
		if(  userDao.insert( user ) == 1 ) {
			return user.getId();
		}else {
			return -1l;
		}
	}

}
