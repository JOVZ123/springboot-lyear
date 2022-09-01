package com.lanyuan.springbootlyear.controller;

import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YRole;
import com.lanyuan.springbootlyear.pojo.YUser;
import com.lanyuan.springbootlyear.service.RoleService;
import com.lanyuan.springbootlyear.uitl.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    @RequestMapping(value = "/roleShow",method = RequestMethod.GET)
    public R roleShow(HttpSession session, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "4") Integer pageSize, String search){
        Map<String,Object> map = new HashMap<>();
        if (search==null){
            search = (String) session.getAttribute("search");
        }else {
            session.setAttribute("search",search);
        }
        PageInfo<YRole> roleList = roleService.roleShow(pageNum, pageSize, search);
        session.setAttribute("roleList",roleList);
        map.put("roleList",roleList);
        return R.ok().data(map);
    }
    @RequestMapping(value = "/selectByRolename")
    public R selectByRolename(String rolename){
        Map<String,Object> map = new HashMap<>();
        YRole yRole = roleService.selectByRolename(rolename);
        if (yRole==null){
            map.put("success","角色可用");
            return R.ok().data(map);
        }else {
            map.put("error","角色已存在");
            return R.error().data(map);
        }
    }
    @RequestMapping(value = "/roleDel",method = RequestMethod.DELETE)
    public R roleDel(Integer[] id){
        Map<String ,Object> map = new HashMap<>();
        Set<YRole> roles = roleService.selectById(id);
        int i = roleService.delete(id);
        if (i>0){
            map.put("删除的角色信息",roles);
           return R.ok().data(map);
        }else {
            map.put("删除失败","error");
            return R.error().data(map);
        }

    }
    @RequestMapping(value = "/roleUpd",method = RequestMethod.POST)
    public R roleUpd(YRole role){
        Map<String ,Object> map = new HashMap<>();
        int i = roleService.roleUpd(role);
        YRole r = roleService.selectId(role.getId());
        if (i>0){
            map.put("修改的角色信息",r);
            return R.ok().data(map);
        }else {
            map.put("修改失败","error");
            return R.error().data(map);
        }
    }
    @PostMapping("/roleAdd")
    public R roleAdd(YRole role){
        Map<String ,Object> map = new HashMap<>();
        role.setCreatetime(new Date());
        int i = roleService.roleAdd(role);
        if (i>0){
            map.put("新增成功","success");
            return R.ok().data(map);
        }else {
            map.put("新增失败","error");
            return R.ok().data(map);
        }
    }
}
