package com.lanyuan.springbootlyear.service;

import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YUser;

import java.util.List;
import java.util.Set;

public interface UserService {
    int register(YUser user);
    YUser login(YUser yUser);
    YUser selectByUsername(String username);

    PageInfo<YUser> show(Integer pageNum,Integer pageSize,String search);
    YUser selectId(Integer id); //单个用户信息
    Set<YUser> selectById(Integer[] id); //多个用户信息
    int userUpd(YUser yuser);
    int userDel(Integer ids[]);
    int delete(Integer[] id);
    int relation(Integer uid, Integer[] rid);
    int removerelation(Integer userid);

    int disable(Integer[] id);
    List<YUser> show();
    int open(Integer[] id);

}
