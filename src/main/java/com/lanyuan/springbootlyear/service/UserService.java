package com.lanyuan.springbootlyear.service;

import com.lanyuan.springbootlyear.pojo.YUser;

public interface UserService {
    int register(YUser user);
    YUser login(YUser yUser);
    YUser selectByUsername(String username);
}
