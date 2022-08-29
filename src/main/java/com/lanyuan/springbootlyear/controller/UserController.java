package com.lanyuan.springbootlyear.controller;

import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.UserService;

import com.lanyuan.springbootlyear.uitl.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;
   @RequestMapping(value = "/login",method = RequestMethod.POST)
   @ResponseBody
   public R login(YUser yuser, String code,HttpSession session){
       String randomCode = (String) session.getAttribute("randomCode");
       Map<String,Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(yuser.getAccount(), yuser.getPassword());
        if (randomCode.equalsIgnoreCase(code)) {
            try {
                subject.login(usernamePasswordToken);
                session.setAttribute("admin",subject.getPrincipal());
                map.put("用户信息",subject.getPrincipal());
                return R.ok().data(map);
            } catch (AuthenticationException e) {
                map.put("登录失败","error");
            }
        }else {
           map.put("error","验证码输入有误");
       }
       return R.error().data(map);
   }
   @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(YUser yUser,String code,HttpSession session){
       String randomCode = (String) session.getAttribute("randomCode");
       Map<String,Object> map = new HashMap<>();
       if (randomCode.equalsIgnoreCase(code)) {
           yUser.setCreatetime(new Date());
           SimpleHash password = new SimpleHash(
                   "md5",
                   yUser.getPassword(),
                   yUser.getCreatetime().getTime()+"",
                   1024);
           yUser.setPassword(String.valueOf(password));
           int register = userService.register(yUser);
           if (register>0){
               return "redirect:/toLogin";
           }
       }
       return "redirect:/toRegister";
   }
   @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public String logout(HttpSession session){
       session.invalidate();
       return "redirect:/toLogin";
   }
}
