package com.lanyuan.springbootlyear.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.mapper.YUserMapper;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.UserService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public PageInfo<YUser> show(Integer pageNum, Integer pageSize, String search) {
        PageHelper.startPage(pageNum,pageSize);
        YUser yUser = new YUser();
        yUser.setAccount(search);
        yUser.setName(search);
        yUser.setPhone(search);
        List<YUser> show = mapper.show(yUser);
        List<YUser> list = new ArrayList<>();
        for (YUser user : show) {
            if ("1".equals(user.getDel())){
                list.add(user);
            }
        }
        return new PageInfo<>(list);
    }
    @Override
    public int delete(Integer[] id) {
        int n =0;
        for (Integer i : id) {
            n+=mapper.deleteByPrimaryKey(i);
        }
        if (n>0){
            return n;
        }
        return -1;
    }

    @Override
    public YUser selectId(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }
    @Override
    public Set<YUser> selectById(Integer[] id) {
        Set<YUser> set = new HashSet<>();
        for (Integer i : id) {
            YUser user = mapper.selectByPrimaryKey(i);
            set.add(user);
        }
        return set;
    }
    @Override
    public int userUpd(YUser user) {
        return mapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int relation(Integer uid, Integer[] rid) {
        int n=0;
        for (Integer roleid : rid) {
            n += mapper.insertRelation(uid, roleid);
        }
        if (n<=0){
            return -1;
        }
        return n;
    }

    @Override
    public int userDel(Integer[] ids) {
        int n=0;
        YUser yUser = new YUser();
        for (Integer id : ids) {
            yUser.setDel("0");
            yUser.setId(id);
            n += mapper.updateByPrimaryKeySelective(yUser);
        }
        if (n<=0){
            return -1;
        }
        return n;
    }

    @Override
    public int removerelation(Integer userid) {
        return mapper.removerelation(userid);
    }

    @Override
    public int disable(Integer[] id) {
        int n = 0;
        for (Integer i : id) {
            YUser u = mapper.selectByPrimaryKey(i);
            if ("1".equals(u.getStatus())) {
                u.setStatus("0");
                n += mapper.updateByPrimaryKeySelective(u);
            }else {
                continue;
            }
        }
        if(n<=0){
            return -1;
        }
        return n;
    }

    @Override
    public int open(Integer[] id) {
        int n = 0;
        for (Integer i : id) {
            YUser u = mapper.selectByPrimaryKey(i);
            if ("0".equals(u.getStatus())) {
                u.setStatus("1");
                n += mapper.updateByPrimaryKeySelective(u);
            }else {
                continue;
            }
        }
        if (n<=0){
            return -1;
        }
        return n;
    }
}
