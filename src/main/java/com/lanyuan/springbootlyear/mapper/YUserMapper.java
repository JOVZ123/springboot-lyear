package com.lanyuan.springbootlyear.mapper;

import com.lanyuan.springbootlyear.pojo.YUser;
import org.springframework.stereotype.Repository;

@Repository
public interface YUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YUser record);

    int insertSelective(YUser record);

    YUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YUser record);

    int updateByPrimaryKey(YUser record);
    YUser login(YUser yUser);

    YUser selectByUsername(String username);
}