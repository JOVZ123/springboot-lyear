package com.lanyuan.springbootlyear.controller;
import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.UserService;
import com.lanyuan.springbootlyear.uitl.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.awt.image.GifImageDecoder;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    UserService userService;
   @RequestMapping(value = "/login",method = RequestMethod.POST)
   public R login(YUser yuser, String code,HttpSession session){
       Map<String,Object> map = new HashMap<>();
       String randomCode = (String) session.getAttribute("randomCode");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(yuser.getAccount(), yuser.getPassword());
        if (randomCode!=null){
            if (!randomCode.equalsIgnoreCase(code)) {
                map.put("error","验证码输入有误");
                return R.error().data(map);
            }else {
                try {
                    subject.login(usernamePasswordToken);
                    YUser admin = (YUser) subject.getPrincipal();
                    if (admin.getDel().equals("1")) {
                        if (admin.getStatus().equals("1")) {
                            session.setAttribute("admin", admin);
                            map.put("success", "登录成功");
                            map.put("当前登录的用户信息", subject.getPrincipal());
                            return R.ok().data(map);
                        }else {
                            map.put("error","您的账号已被禁用，请联系管理员");
                            return R.error().data(map);
                        }
                    }else {
                        map.put("error","当前用户不存在");
                        return R.error().data(map);
                    }
                } catch (AuthenticationException e) {
                    map.put("error","密码错误，登录失败");
                    return R.error().data(map);
                }
            }
       }else {
            map.put("error","验证码不可为空");
            return R.error().data(map);
        }
   }
   @RequestMapping(value = "/register",method = RequestMethod.POST)
   public R register(YUser yUser,String code,HttpSession session){
       Map<String,Object> map = new HashMap<>();
       YUser user = userService.selectByUsername(yUser.getAccount());
       String randomCode = (String) session.getAttribute("randomCode");
       if (user==null){
           if (randomCode!=null) {
               if (randomCode.equalsIgnoreCase(code)) {
                   yUser.setCreatetime(new Date());
                   SimpleHash password = new SimpleHash(
                           "md5",
                           yUser.getPassword(),
                           yUser.getCreatetime().getTime() + "",
                           1024);
                   yUser.setPassword(String.valueOf(password));
                   int register = userService.register(yUser);
                   if (register > 0) {
                       map.put("success", "注册成功");
                       map.put("注册用户信息", yUser);
                       return R.ok().data(map);
                   } else {
                       map.put("error", "注册失败");
                       return R.error().data(map);
                   }
               } else {
                   map.put("error", "验证码输入有误");
                   return R.error().data(map);
               }

           }else {
               map.put("error", "验证码不可为空");
               return R.error().data(map);
           }
       }else {
           map.put("error", "当前账号已存在");
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
       } else {
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
       if (yUser.getAccount()!=null&&yUser.getAccount().length()>0) {
           YUser user = userService.selectByUsername(yUser.getAccount());
           if (user == null) {
               if (yUser.getPassword().length()>0) {
                   yUser.setCreatetime(new Date());
                   SimpleHash password = new SimpleHash(
                           "md5",
                           yUser.getPassword(),
                           yUser.getCreatetime().getTime() + "",
                           1024
                   );
                   yUser.setPassword(String.valueOf(password));
                   int userAdd = userService.register(yUser);
                   if (userAdd <= 0) {
                       map.put("error", "新增失败");
                       return R.error().data(map);
                   } else {
                       map.put("success", "新增成功");
                       return R.ok().data(map);
                   }
               }else {
                   map.put("error", "密码不可为空");
                   return R.error().data(map);
               }
           } else {
               map.put("error", "账号已存在");
               return R.error().data(map);
           }
       }else {
           map.put("error", "账号不可为空");
           return R.error().data(map);
       }

   }
   @RequestMapping(value = "/userDel",method = RequestMethod.DELETE)
    public R userDel(Integer[] id){
       Map<String,Object> map = new HashMap<>();
       //修改字段进行删除 ，数据库还是存在
       if (id.length>0) {
           int i = userService.userDel(id);
           if (i>0) {
               Set<YUser> yUsers = userService.selectById(id);
               map.put("删除的用户信息", yUsers);
               return R.ok().data(map);
           }else {
               map.put("error","删除失败");
               return R.error().data(map);
           }
       }else {
           map.put("error","没有id传值");
           return R.error().data(map);
       }
   }
   @RequestMapping(value = "/userUpd",method = RequestMethod.PUT)
    public R userUpd(YUser yUser){
       Map<String,Object> map = new HashMap<>();
       YUser u = userService.selectId(yUser.getId());
       if (u!=null) {
           yUser.setCreatetime(new Date());
           SimpleHash password = new SimpleHash("md5",
                   yUser.getPassword(),
                   yUser.getCreatetime().getTime() + "",
                   1024);
           yUser.setPassword(String.valueOf(password));
           int i = userService.userUpd(yUser);
           YUser user = userService.selectId(yUser.getId());
           if (i <= 0) {
               map.put("error", "修改失败");
               return R.error().data(map);
           } else {
               map.put("修改后的用户信息", user);
               return R.ok().data(map);
           }
       }else {
           map.put("error","该用户不存在");
           return R.error().data(map);
       }
   }
   @RequestMapping(value = "/userRoleRelation",method = RequestMethod.POST)
   public R userRoleRelation(Integer userid,Integer[] roleid){
       Map<String,Object> map = new HashMap<>();
       YUser user = userService.selectId(userid);
       if (user!=null) {
           if (userid != null) {
               map.put("用户" + user.getAccount() + "初始绑定的角色信息", user.getyRoles());
               //先删除原来的关系表
               userService.removerelation(userid);
               //再添加现有的
               int relation = userService.relation(userid, roleid);
               if (relation > 0) {
                   user = userService.selectId(userid);
                   map.put("用户" + user.getAccount() + "改变后绑定的角色信息", user.getyRoles());
                   return R.ok().data(map);
               } else {
                   map.put("error", "关联失败");
                   return R.error().data(map);
               }
           } else {
               map.put("error", "用户id不可为空或不存在");
               return R.error().data(map);
           }
       }else {
           map.put("error", "该用户不存在");
           return R.error().data(map);
       }
   }

    @RequestMapping(value = "/userDisable",method = RequestMethod.PUT)
    public R disable(Integer[] id) {
        Map<String,Object> map = new HashMap<>();
        if (id!=null&&id.length>0) {
            Set<YUser> yUsers = userService.selectById(id);
            for (YUser yUser : yUsers) {
                if (yUser!=null){
                    int n = userService.disable(id);
                    if (n<=0){
                        map.put("error","禁用失败");
                        return R.error().data(map);
                    }else {
                        map.put("success", "禁用成功");
                        map.put("禁用的用户信息", userService.selectById(id));
                        return R.ok().data(map);
                    }
                }else {
                    map.put("error","id不存在");
                    return R.error().data(map);
                }
            }
            map.put("success","存在id");
            return R.ok().data(map);
        }else {
            map.put("error","没有传id值");
            return R.error().data(map);
        }
    }

    @RequestMapping(value = "/userOpens",method = RequestMethod.PUT)
    public R open(Integer[] id) {
        Map<String,Object> map = new HashMap<>();
        if (id!=null&&id.length>0) {
            Set<YUser> yUsers = userService.selectById(id);
            for (YUser yUser : yUsers) {
                if (yUser!=null){
                    int n = userService.open(id);
                    if (n<=0){
                        map.put("error","启用失败");
                        return R.error().data(map);
                    }else {
                        map.put("success", "启用成功");
                        map.put("启用的用户信息", userService.selectById(id));
                        return R.ok().data(map);
                    }
                }else {
                    map.put("error","id不存在");
                    return R.error().data(map);
                }
            }
            map.put("success","存在id");
            return R.ok().data(map);
        }else {
            map.put("error","没有id传值");
            return R.error().data(map);
        }
    }
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public R  logout(HttpSession session){
        Map<String,Object> map = new HashMap<>();
        YUser user= (YUser) session.getAttribute("admin");
        if (user!=null) {
            map.put("success", "退出登录成功");
            return R.ok().data(map);
        }else {
            map.put("error","未登录");
            return R.error().data(map);
        }
    }

}
