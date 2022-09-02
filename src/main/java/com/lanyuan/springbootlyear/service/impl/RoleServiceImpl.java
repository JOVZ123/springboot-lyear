package com.lanyuan.springbootlyear.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.mapper.YRoleMapper;
import com.lanyuan.springbootlyear.pojo.YRole;
import com.lanyuan.springbootlyear.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    YRoleMapper mapper;
    @Override
    public Set<YRole> selectById(Integer[] id) {
        Set<YRole> set = new HashSet<>();
        for (Integer i : id) {
            YRole yRole = mapper.selectByPrimaryKey(i);
            set.add(yRole);
        }
        return set;
    }

    public PageInfo<YRole> roleShow(Integer pageNum, Integer pageSize, String search) {
        PageHelper.startPage(pageNum,pageSize);
        YRole role = new YRole();
        role.setReloname(search);
        List<YRole> show = mapper.show(role);
        return new PageInfo<>(show);
    }

    @Override
    public YRole selectByRolename(String rolename) {
        return mapper.selectByRolename(rolename);
    }

    @Override
    public YRole selectId(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int roleUpd(YRole yRole) {
        return mapper.updateByPrimaryKeySelective(yRole);
    }

    @Override
    public int roleAdd(YRole yRole) {
        return mapper.insertSelective(yRole);
    }

    @Override
    public int delete(Integer[] id) {
        int n=0;
        for (Integer i : id) {
            mapper.removerelation(i);
            n+=mapper.deleteByPrimaryKey(i);
        }
        if (n<=0){
            return -1;
        }
        return n;
    }

    @Override
    public List<YRole> roleAll() {
        return mapper.roleAll();
    }
}
