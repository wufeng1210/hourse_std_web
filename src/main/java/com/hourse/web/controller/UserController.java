package com.hourse.web.controller;

import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
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



    @RequestMapping("manager")
    public String manager() {
        return "userManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(int userId, String userName) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            User qryUser = new User();
            qryUser.setUserId(userId);
            qryUser.setUserName(userName);
            List<User> userList = userService.queryList(qryUser);
            int total = userService.count(qryUser);
            resMap.put("total", total);
            resMap.put("rows",userList);
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public Map<String,Object> saveOrUpdate(int userId, String userName,String userPassWord) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int saveNums = 0;
            User opUser = new User();
            opUser.setUserId(userId);
            opUser.setUserName(userName);
            opUser.setUserPassWord(userPassWord);
            opUser.setUserType("0");
            opUser.setSecretKey(userPassWord);
            if( -1 != userId){
                saveNums=userService.update(opUser);
            }else{
                saveNums=userService.save(opUser);
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
    public Map<String,Object> modifyPassword(String userId,String oldPassword,String newPassword) {
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
