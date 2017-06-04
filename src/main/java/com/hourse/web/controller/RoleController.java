package com.hourse.web.controller;

import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private IUserRoleService userRoleService;



    @RequestMapping("manager")
    public String manager() {
        return "roleManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(int roleId, String roleName) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            UserRole qryUserRole = new UserRole();
            qryUserRole.setRoleId(roleId);
            qryUserRole.setRoleName(roleName);
            List<UserRole> roleList = userRoleService.queryList(qryUserRole);
            int total = userRoleService.count(qryUserRole);
            resMap.put("total", total);
            resMap.put("rows",roleList);
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public Map<String,Object> saveOrUpdate(int roleId, String roleName) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int saveNums = 0;
            UserRole opUserRole = new UserRole();
            opUserRole.setRoleId(roleId);
            opUserRole.setRoleName(roleName);
            opUserRole.setAuthIds("1");
            opUserRole.setRoleDescription("");
            if( -1 != roleId){
                saveNums=userRoleService.update(opUserRole);
            }else{
                saveNums=userRoleService.save(opUserRole);
            }
            if(saveNums>0){
                resMap.put("success", true);

            }else{
                resMap.put("success", false);
                resMap.put("errorMsg", "保存失败");
            }
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("delete")
    public Map<String,Object> delete(String delIds) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int delNums = 0;
            delNums=userRoleService.delete(delIds);
            if(delNums>0){
                resMap.put("success", true);
                resMap.put("delNums", delNums);
            }else{
                resMap.put("success", false);
                resMap.put("errorMsg", "删除失败");
            }
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("authMenu")
    public Map<String,Object> authMenu(String roleId) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
//
        }catch (Exception e){

        }
        return resMap;
    }
}
