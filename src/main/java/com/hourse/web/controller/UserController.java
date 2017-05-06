package com.hourse.web.controller;

import com.hourse.web.model.User;
import com.hourse.web.model.UserRole;
import com.hourse.web.service.IUserAuthService;
import com.hourse.web.service.IUserRoleService;
import com.hourse.web.service.IUserService;
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
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;



    @ResponseBody
    @RequestMapping("userManager")
    public ModelAndView getuserInfo(User user) {
        ModelAndView modelAndView = new ModelAndView();
        try{
            List<User> userList = userService.find(new User());
            modelAndView.setViewName("userManager");
//            modelAndView.addObject("userList",userList);
        }catch (Exception e){

        }
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("userList")
    public Map<String,Object> user(User user) {
        Map<String,Object> resMap = new HashMap<String, Object>();
        try{
            List<User> userList = userService.find(new User());
            int total = userService.count(new User());
            resMap.put("total", total);
            resMap.put("rows",userList);
        }catch (Exception e){

        }
        return resMap;
    }
}
