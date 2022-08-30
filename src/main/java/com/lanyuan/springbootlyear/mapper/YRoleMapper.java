package com.lanyuan.springbootlyear.mapper;

import com.lanyuan.springbootlyear.pojo.YRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YRole record);

    int insertSelective(YRole record);

    YRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YRole record);

    int updateByPrimaryKey(YRole record);

    List<YRole> selectByUid(Integer uid);
}