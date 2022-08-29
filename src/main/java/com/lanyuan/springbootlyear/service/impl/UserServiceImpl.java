package com.lanyuan.springbootlyear.service.impl;

import com.lanyuan.springbootlyear.mapper.YUserMapper;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    YUserMapper mapper;
    @Override
    public int register(YUser yuser) {
        return mapper.insertSelective(yuser);
    }

    @Override
    public YUser login(YUser yUser) {
        return mapper.login(yUser);
    }

    @Override
    public YUser selectByUsername(String username) {
        return mapper.selectByUsername(username);
    }
}
