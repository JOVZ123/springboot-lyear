package com.lanyuan.springbootlyear.controller;

import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.UserService;

import com.lanyuan.springbootlyear.uitl.R;
import com.lanyuan.springbootlyear.uitl.UploadDownloadUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@ResponseBody
public class UserController {
    @Autowired
    UserService userService;
   @RequestMapping(value = "/login",method = RequestMethod.POST)
   public R login(YUser yuser, String code,HttpSession session){
       Map<String,Object> map = new HashMap<>();
       String randomCode = (String) session.getAttribute("randomCode");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(yuser.getAccount(), yuser.getPassword());
        if (!randomCode.equalsIgnoreCase(code)) {
            map.put("error","验证码输入有误");
            return R.error().data(map);
        }else {
            try {
                subject.login(usernamePasswordToken);
                session.setAttribute("admin",subject.getPrincipal());
                map.put("success","登录成功");
                map.put("当前登录的用户信息",subject.getPrincipal());
                return R.ok().data(map);
            } catch (AuthenticationException e) {
                map.put("error","登录失败");
                return R.error().data(map);
            }

       }
   }
   @RequestMapping(value = "/register",method = RequestMethod.POST)
   public R register(YUser yUser,String code,HttpSession session){
       Map<String,Object> map = new HashMap<>();
       String randomCode = (String) session.getAttribute("randomCode");
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
               map.put("success","注册成功");
               map.put("注册用户信息",yUser);
               return R.ok().data(map);
           }else {
               map.put("error","注册失败");
               return R.error().data(map);
           }
       }else {
           map.put("error","验证码输入有误");
           return R.error().data(map);
       }
   }
   @RequestMapping(value = "/selectByAccount")
   public R selectByAccount(String account){
       Map<String,Object> map = new HashMap<>();
       YUser user = userService.selectByUsername(account);
       if (user==null){
           map.put("success","账号可用");
           return R.ok().data(map);
       }else {
           map.put("error","账号已被注册");
           return R.error().data(map);
       }
   }

   @PostMapping("/userShow")
   public R show(HttpSession session, String search, @RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "4")Integer pageSize){
       Map<String,Object> map = new HashMap<>();
       if (search==null){
           search = (String) session.getAttribute("search");
       }else {
           session.setAttribute("search",search);
       }
       PageInfo<YUser> adminList = userService.show(pageNum,pageSize,search);
       //session.setAttribute("adminList",adminList);
       map.put("adminList",adminList);
       return R.ok().data(map);
   }
   @PostMapping("/userAdd")
    public R userAdd(YUser yUser){
       Map<String,Object> map = new HashMap<>();
       yUser.setCreatetime(new Date());
       SimpleHash password = new SimpleHash(
               "md5",
               yUser.getPassword(),
               yUser.getCreatetime().getTime()+"",
               1024
       );
       yUser.setPassword(String.valueOf(password));
       int userAdd = userService.register(yUser);
       if (userAdd<=0){
           map.put("error","新增失败");
           return R.error().data(map);
       }else {
           map.put("success", "新增成功");
           return R.ok().data(map);
       }
   }
   @RequestMapping(value = "/userDel",method = RequestMethod.DELETE)
    public R userDel(Integer[] id){
       Map<String,Object> map = new HashMap<>();
       //修改字段进行删除 ，数据库还是存在
       int i = userService.userDel(id);
       Set<YUser> yUsers = userService.selectById(id);
       map.put("删除的用户信息",yUsers);
       return R.ok().data(map);
   }
   @RequestMapping(value = "/userUpd",method = RequestMethod.POST)
    public R userUpd(YUser yUser){
       Map<String,Object> map = new HashMap<>();
       yUser.setCreatetime(new Date());
       SimpleHash password=new SimpleHash("md5",
               yUser.getPassword(),
               yUser.getCreatetime().getTime()+"",
               1024);
       yUser.setPassword(String.valueOf(password));
       int i = userService.userUpd(yUser);
       YUser user = userService.selectId(yUser.getId());
       if (i<=0){
           map.put("error","修改失败");
           return R.error().data(map);
       }else {
           map.put("修改后的用户信息", user);
           return R.ok().data(map);
       }
   }
   @RequestMapping(value = "/userRoleRelation",method = RequestMethod.POST)
   public R userRoleRelation(Integer userid,Integer[] roleid){
       Map<String,Object> map = new HashMap<>();
       YUser user = userService.selectId(userid);
        map.put("用户"+user.getAccount()+"初始绑定的角色信息",user.getyRoles());
       //先删除原来的关系表
       userService.removerelation(userid);

       //再添加现有的
       userService.relation(userid,roleid);
       user = userService.selectId(userid);
       map.put("用户"+user.getAccount()+"改变后绑定的角色信息",user.getyRoles());
       return R.ok().data(map);
   }

    @RequestMapping(value = "/userDisable",method = RequestMethod.GET)
    public R disable(Integer[] id) {
        Map<String,Object> map = new HashMap<>();
        int n=0;
        if (id != null) {
             n = userService.disable(id);
        }
        if (n<=0){
            map.put("error","禁用失败");
            return R.error().data(map);
        }
        map.put("success","禁用成功");
        map.put("禁用的用户信息",userService.selectById(id));
        return R.ok().data(map);
    }

    @RequestMapping(value = "/userOpens",method = RequestMethod.GET)
    public R open(Integer[] id) {
        Map<String,Object> map = new HashMap<>();
        int n=0;
        if (id != null) {
            n = userService.open(id);
        }
        if (n<=0){
            map.put("error","启用失败");
            return R.error().data(map);
        }
        map.put("success","启用成功");
        map.put("启用的用户信息",userService.selectById(id));
        return R.ok().data(map);
    }

}
