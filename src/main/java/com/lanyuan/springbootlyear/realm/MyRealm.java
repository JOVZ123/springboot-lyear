package com.lanyuan.springbootlyear.realm;

import com.lanyuan.springbootlyear.pojo.YRole;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

//需要继承一个类
public class MyRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    UserService userService;
    //权限认证
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录成功后session存储的用户信息
        YUser admin = (YUser) principalCollection.getPrimaryPrincipal();
        //创建权限信息对象(包含了有哪些权限地址 哪些角色名称)
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //负责存储角色名称  如果想存储多个角色需要传递一个List<String>
//        if (admin.getRole()!=null)info.addRole(admin.getRole().getRolename());
//        //负责存储权限地址
//        List<String> list = new ArrayList<>();
//        if (admin.getRole()!=null) {
//            for (OMenu m : admin.getRole().getMenus()) {
//                list.add(m.getUrl());
//            }
//        }
        List<String> list = new ArrayList<>();
        if (admin.getyRoles()!=null) {
            for (YRole yRole : admin.getyRoles()) {
                list.add(yRole.getStatus());
            }
        }
        info.addStringPermissions(list);
        return info;
    }

    //身份认证:登录时判断用户身份的
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户名
        String username= (String) authenticationToken.getPrincipal();
        //根据用户名查询用户信息
        YUser admin = userService.selectByUsername(username);
        if (admin==null) return null; //null表示登录不通过
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                admin,
                admin.getPassword(),
                new SimpleByteSource(admin.getCreatetime().getTime()+""),
                "myRealm"
        );
        return info;
    }
}
