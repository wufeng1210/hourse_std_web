package com.hourse.web.controller;

import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;



    @RequestMapping("manager")
    public String manager() {
        return "userManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(User user) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            List<User> userList = userService.queryList(user);
            List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
            for(User u : userList){
                Map<String,Object> m = MapUtil.toMap(u);
                if(u.getRoleId() !=0) {
                    m.put("roleName", userRoleService.query(u.getRoleId()).getRoleName());
                }
                m.put("userDescription", u.getUserDescription());
                resList.add(m);
            }
            int total = userService.count(user);
            resMap.put("total", total);
            resMap.put("rows",resList);
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public Map<String,Object> saveOrUpdate(User user) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int saveNums = 0;
            user.setUserType("0");
            user.setSecretKey(user.getUserPassWord());
            if( -1 != user.getUserId()){
                saveNums=userService.update(user);
            }else{
                saveNums=userService.save(user);
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
            delNums=userService.delete(delIds);
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
    @RequestMapping("modifyPassword")
    public Map<String,Object> modifyPassword(int userId,String oldPassword,String newPassword) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            User user=userService.query(userId);
            if(user == null || !user.getUserPassWord().equals(oldPassword)){
                resMap.put("success", "true");
                resMap.put("errorMsg", "账号不存在或原密码错误");
            }else {
                user.setUserPassWord(newPassword);
                int updateNum = userService.update(user);
                if (updateNum > 0) {
                    resMap.put("success", "true");
                } else {
                    resMap.put("success", "true");
                    resMap.put("errorMsg", "修改失败");
                }
            }
        }catch (Exception e){

        }
        return resMap;
    }
}
