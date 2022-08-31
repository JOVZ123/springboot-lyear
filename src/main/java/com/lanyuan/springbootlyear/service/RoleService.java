package com.lanyuan.springbootlyear.service;

import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YRole;

import java.util.Set;

public interface RoleService {
    Set<YRole> selectById(Integer[] id);
    PageInfo<YRole> roleShow(Integer pageNum,Integer pageSize,String search);
    YRole selectId(Integer id);
    int roleUpd(YRole yRole);
    int roleAdd(YRole yRole);
    int delete(Integer[] id);
}
