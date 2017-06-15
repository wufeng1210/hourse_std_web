package com.hourse.web.controller;

import com.hourse.web.model.User;
import com.hourse.web.model.UserAuth;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
import com.hourse.web.util.CookieUtil;
import com.hourse.web.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wufeng on 2017/4/10.
 */
@Controller
@RequestMapping("auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IUserAuthService userAuthService;
    @Autowired
    private IUserRoleService userRoleService;



    @ResponseBody
    @RequestMapping("getAuth")
    public List<Map<String,Object>> getAuth(User user) {
        List<Map<String,Object>> resList = new ArrayList<Map<String, Object>>();
        try{
            UserRole userRole = userRoleService.getUserRoleByRoleId(user.getRoleId());
//            UserRole userRole = userRoleService.getUserRoleByRoleId(1);
            resList = userAuthService.getAuthsByParentId("-1",userRole.getAuthIds());
        }catch (Exception e){

        }
        return resList;
    }

    @RequestMapping("manager")
    public String manager() {
        return "authManager";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> list(UserAuth userAuth) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            List<UserAuth> userAuthList = userAuthService.queryList(userAuth);
            int total = userAuthService.count(userAuth);
            resMap.put("total", total);
            resMap.put("rows",userAuthList);
        }catch (Exception e){

        }
        return resMap;
    }

    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public Map<String,Object> saveOrUpdate(UserAuth userAuth) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            int saveNums = 0;
            if( -1 != userAuth.getAuthId()){
                saveNums=userAuthService.update(userAuth);
            }else{
                saveNums=userAuthService.save(userAuth);
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
            delNums=userAuthService.delete(delIds);
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
}
