package com.lanyuan.springbootlyear.controller;
import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YRole;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.RoleService;
import com.lanyuan.springbootlyear.service.UserService;
import com.lanyuan.springbootlyear.uitl.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
   @RequestMapping(value = "/login",method = RequestMethod.POST)
   public R login(YUser yuser, String code,HttpSession session){
       Map<String,Object> map = new HashMap<>();
       if (yuser.getAccount()!=null&&yuser.getAccount().length()>0) {
           if (yuser.getPassword()!=null&&yuser.getPassword().length()>0) {
               String randomCode = (String) session.getAttribute("randomCode");
               Subject subject = SecurityUtils.getSubject();
               UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(yuser.getAccount(), yuser.getPassword());
               if (code!=null&&code.length()>0) {
                   if (!randomCode.equalsIgnoreCase(code)) {
                       map.put("error", "验证码输入有误");
                       return R.error().data(map);
                   } else {
                       try {
                           subject.login(usernamePasswordToken);
                           YUser admin = (YUser) subject.getPrincipal();
                           if (admin.getDel().equals("1")) {
                               if (admin.getStatus().equals("1")) {
                                   session.setAttribute("admin", admin);
                                   map.put("success", "登录成功");
                                   map.put("当前登录的用户信息", subject.getPrincipal());
                                   return R.ok().data(map);
                               } else {
                                   map.put("error", "您的账号已被禁用，请联系管理员");
                                   return R.error().data(map);
                               }
                           } else {
                               map.put("error", "当前用户不存在");
                               return R.error().data(map);
                           }
                       } catch (AuthenticationException e) {
                           map.put("error", "密码错误，登录失败");
                           return R.error().data(map);
                       }
                   }
               } else {
                   map.put("error", "验证码不可为空");
                   return R.error().data(map);
               }
           }else {
               map.put("error", "密码不可为空");
               return R.error().data(map);
           }
       }else {
           map.put("error", "账号不可为空");
           return R.error().data(map);
       }
   }

   @RequestMapping(value = "/register",method = RequestMethod.POST)
   public R register(YUser yUser,String code,HttpSession session){
       Map<String,Object> map = new HashMap<>();
           if (yUser.getAccount()!=null&&yUser.getAccount().length() > 0) {
               YUser user = userService.selectByUsername(yUser.getAccount());
               String randomCode = (String) session.getAttribute("randomCode");
               if (user == null) {
                   if (yUser.getPassword()!=null&&yUser.getPassword().length() > 0) {
                       if (code!=null&&code.length()>0) {
                           if (randomCode.equalsIgnoreCase(code)) {
                               if (yUser.getPassword().length() > 0) {
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
                                   map.put("error", "密码不可为空");
                                   return R.error().data(map);
                               }
                           } else {
                               map.put("error", "验证码输入有误");
                               return R.error().data(map);
                           }

                       } else {
                           map.put("error", "验证码不可为空");
                           return R.error().data(map);
                       }
                   } else {
                       map.put("error", "密码不可为空");
                       return R.error().data(map);
                   }
               } else {
                   map.put("error", "当前账号已存在");
                   return R.error().data(map);
               }
           } else {
               map.put("error", "账号不可以为空");
               return R.error().data(map);
           }
   }
   @RequestMapping(value = "/selectByAccount")
   public R selectByAccount(String account){
       Map<String,Object> map = new HashMap<>();
       if (account!=null) {
           YUser user = userService.selectByUsername(account);
           if (user == null) {
               map.put("success", "账号可用");
               return R.ok().data(map);
           } else {
               map.put("error", "账号已被注册");
               return R.error().data(map);
           }
       }else {
           map.put("error", "你没有传值");
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
               if (yUser.getPassword()!=null&&yUser.getPassword().length()>0) {
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
       if (id!=null&&id.length>0) {
           Set<YUser> users = userService.selectById(id);
           for (YUser user : users) {
               if (user!=null) {
                   if (user.getDel().equals("1")) {
                       int i = userService.userDel(id);
                       if (i > 0) {
                           Set<YUser> yUsers = userService.selectById(id);
                           map.put("删除的用户信息", yUsers);
                           return R.ok().data(map);
                       } else {
                           map.put("error", "删除失败");
                           return R.error().data(map);
                       }
                   } else {
                       map.put("error", "删除失败，该用户已被删除");
                       return R.error().data(map);
                   }
               }else {
                   map.put("error", "删除失败，id不存在");
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
   @RequestMapping(value = "/userUpd",method = RequestMethod.PUT)
    public R userUpd(YUser yUser){
       Map<String,Object> map = new HashMap<>();
       if (yUser.getId()!=null) {
           YUser u = userService.selectId(yUser.getId());
           if (u != null && u.getDel().equals("1")) {
               if (yUser.getAccount()!=null||yUser.getName()!=null||yUser.getPassword()!=null
                       ||yUser.getPhone()!=null||yUser.getEmail()!=null||
                       yUser.getSex()!=null||yUser.getStatus()!=null||yUser.getCreatetime()!=null) {
                   if ("".equals(yUser.getAccount())||"".equals(yUser.getName())||"".equals(yUser.getPassword())||"".equals(yUser.getPhone())
                   ||"".equals(yUser.getEmail())||"".equals(yUser.getSex())||"".equals(yUser.getStatus())) {
                       map.put("error", "传入的参数为空");
                       return R.error().data(map);
                   }else {
                       YUser selectByUsername = userService.selectByUsername(yUser.getAccount());
                       if (selectByUsername == null) {
                           if (yUser.getPassword() != null && yUser.getPassword().length() > 0) {
                               yUser.setCreatetime(new Date());
                               SimpleHash password = new SimpleHash("md5",
                                       yUser.getPassword(),
                                       yUser.getCreatetime().getTime() + "",
                                       1024);
                               yUser.setPassword(String.valueOf(password));
                           }
                           if ((yUser.getSex() != null && !("0".equals(yUser.getSex()) || "1".equals(yUser.getSex()))) || (yUser.getStatus() != null && !("0".equals(yUser.getStatus()) || "1".equals(yUser.getStatus())))) {
                               map.put("error", "修改不合理，性别和状态只能传入'0'或'1'");
                               return R.error().data(map);
                           } else {
                               int i = userService.userUpd(yUser);
                               YUser user = userService.selectId(yUser.getId());
                               if (i <= 0) {
                                   map.put("error", "修改失败");
                                   return R.error().data(map);
                               } else {
                                   map.put("修改后的用户信息", user);
                                   return R.ok().data(map);
                               }
                           }
                       } else {
                           map.put("error", "该用户名已存在，请换个名字");
                           return R.error().data(map);
                       }

                   }
               }else {
                   map.put("error","修改失败，没有传入修改的字段");
                   return R.error().data(map);
               }
           } else {
               map.put("error", "该用户不存在或者被删除了");
               return R.error().data(map);
           }
       }else {
           map.put("error", "没有传入id值");
           return R.error().data(map);
       }
   }
   @RequestMapping(value = "/userRoleRelation",method = RequestMethod.POST)
   public R userRoleRelation(Integer userid,Integer[] roleid){
       Map<String,Object> map = new HashMap<>();
//       if (userid!=null&&!"".equals(userid)) {
           YUser user = userService.selectId(userid);
           if (user != null && user.getDel().equals("1")) {
               if (userid != null&&!"".equals(userid)) {
                   Set<YRole> yRoles = roleService.selectById(roleid);
                   for (YRole yRole : yRoles) {
                       if (yRole!=null) {
                           if (roleid != null && roleid.length > 0) {
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
                               map.put("error", "roleid没有传值");
                               return R.error().data(map);
                           }
                       }else {
                           map.put("error", "角色不存在");
                           return R.error().data(map);
                       }
                   }
                   return R.error().data(map);
               } else {
                   map.put("error", "用户id不可为空或不存在");
                   return R.error().data(map);
               }
           } else {
               map.put("error", "该用户不存在或者被删除了");
               return R.error().data(map);
           }
//       }else {
//           map.put("error", "userid不可为空");
//           return R.error().data(map);
//       }
   }

    @RequestMapping(value = "/userDisable",method = RequestMethod.PUT)
    public R disable(Integer[] id) {
        Map<String,Object> map = new HashMap<>();
        if (id!=null&&id.length>0) {
            Set<YUser> yUsers = userService.selectById(id);
            for (YUser yUser : yUsers) {
                if (yUser!=null&&yUser.getDel().equals("1")){
                    if (!yUser.getStatus().equals("0")) {
                        int n = userService.disable(id);
                        if (n <= 0) {
                            map.put("error", "禁用失败");
                            return R.error().data(map);
                        } else {
                            map.put("success", "禁用成功");
                            map.put("禁用的用户信息", userService.selectById(id));
                            return R.ok().data(map);
                        }
                    }else{
                        map.put("error","用户"+yUser.getAccount()+"已禁用");
                        return R.error().data(map);
                    }
                }else {
                    map.put("error","该用户已被删除");
                    return R.error().data(map);
                }
            }
            return R.error().data(map);
        }else {
            map.put("error","没有传id值或者id为空");
            return R.error().data(map);
        }
    }

    @RequestMapping(value = "/userOpens",method = RequestMethod.PUT)
    public R open(Integer[] id) {
        Map<String,Object> map = new HashMap<>();
        if (id!=null&&id.length>0) {
            Set<YUser> yUsers = userService.selectById(id);
            for (YUser yUser : yUsers) {
                if (yUser != null && yUser.getDel().equals("1")) {
                    if (!yUser.getStatus().equals("1")) {
                        int n = userService.open(id);
                        if (n <= 0) {
                            map.put("error", "启用失败");
                            return R.error().data(map);
                        } else {
                            map.put("success", "启用成功");
                            map.put("启用的用户信息", userService.selectById(id));
                            return R.ok().data(map);
                        }
                    } else {
                        map.put("error", "用户"+yUser.getAccount()+"已启用");
                        return R.error().data(map);

                    }
                }else {
                    map.put("error", "该用户已被删除");
                    return R.error().data(map);
                }
            }
            return R.error().data(map);
        }else {
            map.put("error","没有id传值或者id为空");
            return R.error().data(map);
        }
    }
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public R  logout(HttpSession session){
        Map<String,Object> map = new HashMap<>();
        YUser user= (YUser) session.getAttribute("admin");
        if (user!=null) {
            map.put("success", "退出登录成功");
            session.removeAttribute("admin");
            return R.ok().data(map);
        }else {
            map.put("error","未登录");
            return R.error().data(map);
        }
    }

}
