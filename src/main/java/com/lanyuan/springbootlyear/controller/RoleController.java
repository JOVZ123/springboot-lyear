package com.lanyuan.springbootlyear.controller;
import com.github.pagehelper.PageInfo;
import com.lanyuan.springbootlyear.pojo.YRole;
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
    @RequestMapping(value = "/roleShow",method = RequestMethod.POST)
    public R roleShow(HttpSession session, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "4") Integer pageSize){
        Map<String,Object> map = new HashMap<>();
        PageInfo<YRole> roleList = roleService.roleShow(pageNum, pageSize, null);
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
        if (id!=null&&id.length>0) {
            Set<YRole> roles = roleService.selectById(id);
            for (YRole role:roles) {
                if (role!=null) {
                    int i = roleService.delete(id);
                    if (i > 0) {
                        map.put("删除的角色信息", roles);
                        return R.ok().data(map);
                    } else {
                        map.put("error", "删除失败");
                        return R.error().data(map);
                    }
                }else {
                    map.put("error", "该角色不存在");
                    return R.error().data(map);
                }
            }
            return R.error().data(map);
        } else {
            map.put("error", "没有传id值或者id不可为空");
            return R.error().data(map);
        }

    }
    @RequestMapping(value = "/roleUpd",method = RequestMethod.PUT)
    public R roleUpd(YRole role){
        Map<String, Object> map = new HashMap<>();
        if (role.getId()!=null&&!"".equals(role.getId())) {
            YRole r = roleService.selectId(role.getId());
            if (r!=null) {
                if (role.getReloname()!=null||role.getStatus()!=null) {
                    if ("".equals(role.getStatus())||"".equals(role.getReloname())) {
                        map.put("error", "修改失败，传的参数不可为空");
                        return R.error().data(map);
                    }else {
                        YRole selectByRolename = roleService.selectByRolename(role.getReloname());
                        if (selectByRolename == null) {
                            if (role.getStatus() != null && !("0".equals(role.getStatus()) || "1".equals(role.getStatus()))) {
                                map.put("error", "修改不合理，状态只能传入'0'或'1'");
                                return R.error().data(map);
                            } else {
                                int i = roleService.roleUpd(role);
                                YRole yRole = roleService.selectId(role.getId());
                                if (i > 0) {
                                    map.put("修改的角色信息", yRole);
                                    return R.ok().data(map);
                                } else {
                                    map.put("error", "修改失败");
                                    return R.error().data(map);
                                }
                            }
                        } else {
                            map.put("error", "该角色名已存在，请换个名字");
                            return R.error().data(map);
                        }
                    }
                }else{
                    map.put("error", "修改失败,没有传入修改的字段");
                    return R.error().data(map);
                }
            }else {
                map.put("error", "修改失败,该角色不存在");
                return R.error().data(map);
            }
        }else {
            map.put("error", "修改失败，id不可为空或者不传");
            return R.error().data(map);
        }
    }
    @PostMapping("/roleAdd")
    public R roleAdd(YRole role) {
        Map<String, Object> map = new HashMap<>();
        if (role.getReloname()!=null&&!"".equals(role.getReloname())) {
            YRole yRole = roleService.selectByRolename(role.getReloname());
                if (yRole == null) {
                    role.setCreatetime(new Date());
                    int i = roleService.roleAdd(role);
                    if (i > 0) {
                        map.put("success", "新增成功");
                        return R.ok().data(map);
                    } else {
                        map.put("error", "新增失败");
                        return R.error().data(map);
                    }
                } else {
                    map.put("error", "角色已存在");
                    return R.error().data(map);
                }
        }else {
            map.put("error", "角色名不可为空");
            return R.error().data(map);
        }
    }

}
