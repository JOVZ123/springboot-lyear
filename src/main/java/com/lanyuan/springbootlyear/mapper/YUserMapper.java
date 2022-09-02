package com.lanyuan.springbootlyear.mapper;

import com.lanyuan.springbootlyear.pojo.YUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.ls.LSInput;

import java.util.List;

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

    List<YUser> show(YUser yUser);

    int insertRelation(@Param("uid") Integer uid, @Param("rid") Integer rid);

    int removerelation(Integer userid);
    List<YUser> zshow();
}