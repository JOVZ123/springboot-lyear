package com.lanyuan.springbootlyear.controller;

import com.lanyuan.springbootlyear.uitl.CodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @RequestMapping("/toUserAdd")
    public String toUserAdd(){
        return "/admin/add";
    }
    @RequestMapping("/toRelation")
    public String toRelation(){
        return "/admin/relation";
    }

}
