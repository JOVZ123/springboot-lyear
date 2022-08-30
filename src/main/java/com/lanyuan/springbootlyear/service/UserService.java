package com.lanyuan.springbootlyear.service;

import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YUser;

import java.util.Set;

public interface UserService {
    int register(YUser user);
    YUser login(YUser yUser);
    YUser selectByUsername(String username);
    int delete(Integer[] id);
    PageInfo<YUser> show(Integer pageNum,Integer pageSize,String search);
    YUser selectId(Integer id); //单个用户信息
    Set<YUser> selectById(Integer[] id); //多个用户信息
    int userUpd(YUser yuser);
    int relation(Integer uid, Integer[] rid);
    int userDel(Integer ids[]);

    int removerelation(Integer userid);

    int disable(Integer[] id);

    int open(Integer[] id);

}
