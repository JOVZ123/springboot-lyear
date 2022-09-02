package com.lanyuan.springbootlyear.controller;

import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YRole;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.RoleService;
import com.lanyuan.springbootlyear.service.UserService;
import com.lanyuan.springbootlyear.uitl.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class JspController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @RequestMapping("/toLogin")
    public String toLogin(){

        return "/login";
    }
    @RequestMapping("/toRegister")
    public String  toRegister(){
        return "/register";

    }
    @GetMapping("/getCode")
    public void getCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CodeUtil.processRequest(req, resp);
    }
    @RequestMapping("/toHome")
    public String toHome(){

        return "/home";
    }
    @RequestMapping("/toIndex")
    public String toIndex(){

        return "/index";
    }
     @RequestMapping("/toUserAdd")
    public String toUserUpd(){
        return "/admin/add";

    }
    @RequestMapping("/toUserUpd")
    public String toUserAdd(Integer id,Map map){
        YUser user = userService.selectId(id);
        map.put("yUser",user);
        return "/admin/eidt";

    }
    @RequestMapping("/toRelation")
    public String toRelation(Integer id,Map map){
        YUser user = userService.selectId(id);
        map.put("user",user);
        List<YRole> yRoles = roleService.roleAll();
        map.put("yRoles",yRoles);
        return "/admin/relation";

    }
    @RequestMapping("/toRoleAdd")
    public String toRoleAdd(){
        return "/role/add";

    }
    @RequestMapping("/toRoleUpd")
    public String toRoleUpd(Integer id,Map map){
        YRole yRole = roleService.selectId(id);
        map.put("yRole",yRole);
        return "/role/eidt";

    }
    @RequestMapping("/toRoleList")
    public String toRoleList(){
        return "/role/list";

    }
    @RequestMapping("/user/show")
    public String userShow(HttpSession session,Map map, String search, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "4")Integer pageSize){
        if (search==null){
            search = (String) session.getAttribute("search");
        }else {
            session.setAttribute("search",search);
        }
        PageInfo<YUser> adminList = userService.show(pageNum,pageSize,search);
        map.put("adminList",adminList);
        return "/admin/list";
    }
    @GetMapping("/role/show")
    public String roleShow(Map map, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "4")Integer pageSize){
        PageInfo<YRole> roleList = roleService.roleShow(pageNum,pageSize,null);
        map.put("roleList",roleList);
        return "/role/list";
    }

    @RequestMapping(value = "/user/delete",method = RequestMethod.GET)
    public String userDel(Integer[] id){
        int i = userService.userDel(id);
        return "redirect:/user/show";
    }
    @RequestMapping(value = "/role/delete",method = RequestMethod.GET)
    public String roleDel(Integer[] id){
        int i = roleService.delete(id);
        return "redirect:/role/show";
    }
}
