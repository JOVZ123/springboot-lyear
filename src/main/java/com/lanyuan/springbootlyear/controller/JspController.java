package com.lanyuan.springbootlyear.controller;

import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.uitl.CodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class JspController {
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
    @RequestMapping("/toUserList")
    public String toUserList(@RequestBody @RequestParam Map<String ,PageInfo> adminList,Map map){
        System.out.println(adminList);
        for (String s : adminList.keySet()) {
           // System.out.println(s);
            System.out.println(adminList);
        }
        //System.out.println(pageInfo);
        return "/admin/list";
    }
    @RequestMapping("/toUserAdd")
    public String toUserAdd(){
        return "/admin/add";
    }
    @RequestMapping("/toRelation")
    public String toRelation(){
        return "/admin/relation";
    }
    @RequestMapping("/toRoleList")
    public String toRoleList(){
        return "/role/list";
    }
}
