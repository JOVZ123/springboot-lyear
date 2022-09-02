package com.lanyuan.springbootlyear.config;

import com.lanyuan.springbootlyear.realm.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
    @Bean  //加密方式
    HashedCredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }
    @Bean  //Realm
    MyRealm myRealm(){
        MyRealm myRealm = new MyRealm();
        //注入加密方式
        myRealm.setCredentialsMatcher(credentialsMatcher());
        return myRealm;
    }
    @Bean  //安全管理
    DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        return securityManager;
    }
    @Bean
    ShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition filter = new DefaultShiroFilterChainDefinition();
        filter.addPathDefinition("/js/**","anon");
        filter.addPathDefinition("/login","anon");
        filter.addPathDefinition("/register","anon");
        filter.addPathDefinition("/toRegister","anon");
        filter.addPathDefinition("/selectByAccount","anon");
        filter.addPathDefinition("/image/**","anon");
        filter.addPathDefinition("/css/**","anon");
        filter.addPathDefinition("/fonts/**","anon");
        filter.addPathDefinition("/getCode","anon");
        //filter.addPathDefinition("/logout","logout");
       // filter.addPathDefinition("/**","authc");
        return  filter;
    }
    //@Bean  方言 整合thymeleaf支持shiro标签

}
